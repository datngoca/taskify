import {
  createContext,
  useCallback,
  useEffect,
  useMemo,
  useReducer,
} from "react";
import { boardReducer, initialState } from "./boardReducer";
import { boardApi, taskColumnApi, taskApi } from "../api";
import { BOARD_ACTIONS } from "./actionTypes";
import { mapOrder } from "../utils/sorts";
import { arrayMove } from "@dnd-kit/sortable";

const BoardDataContext = createContext(null);
const BoardDataProvider = ({ children, boardId }) => {
  const [state, dispatch] = useReducer(boardReducer, initialState);
  useEffect(() => {
    const fetchBoardData = async () => {
      try {
        // Giả sử API trả về data chuẩn
        const board = await boardApi.fetchDetails(boardId);

        // Data từ API cần đảm bảo column trong board đã được sort
        board.columns = mapOrder(board.columns, board.columnOrderIds, "id");

        // Data từ API cần đảm bảo cards trong columns đã được sort
        board.columns.forEach((col) => {
          if (!col.tasks) col.tasks = [];
          // Sort cards luôn ở đây để render cho nhẹ
          col.tasks = mapOrder(col.tasks, col.cardOrderIds, "id");
        });

        dispatch({ type: BOARD_ACTIONS.SET_BOARD, payload: board });
      } catch (error) {
        console.error("Lỗi tải board:", error);
        // Có thể dispatch action SET_ERROR để hiện thông báo
      }
    };

    if (boardId) {
      fetchBoardData();
    }
  }, [boardId]);

  // --- B. ACTION CREATORS (ASYNC & OPTIMISTIC UI) ---

  // 1. Thêm Cột Mới
  const createNewColumn = useCallback(
    async (title) => {
      try {
        const newColumnData = await taskColumnApi.createTaskColumn({
          title,
          boardId,
        });
        // Thêm xong thì update state
        dispatch({ type: BOARD_ACTIONS.ADD_COLUMN, payload: newColumnData });
      } catch (error) {
        console.error("Lỗi tạo cột:", error);
      }
    },
    [boardId]
  );

  // 2. Thêm Task Mới
  const createNewCard = useCallback(async (columnId, title) => {
    try {
      const newCardData = await taskApi.create({
        title,
        columnId,
      });
      dispatch({
        type: BOARD_ACTIONS.ADD_TASK,
        payload: { columnId, newTask: newCardData },
      });
    } catch (error) {
      console.error("Lỗi tạo task:", error);
    }
  }, []);

  // 3. Xử lý Kéo thả Cột (Move Column)
  const moveColumns = useCallback(
    async (dndData) => {
      // dndData là kết quả từ handleDragEnd bên BoardContent
      // active, over là 2 object của dnd-kit
      const { active, over } = dndData;

      // Logic tính toán vị trí mới
      const oldIndex = state.columns.findIndex((c) => c.id === active.id);
      const newIndex = state.columns.findIndex((c) => c.id === over.id);

      // Dùng arrayMove của dnd-kit để đổi chỗ
      const newOrderedColumns = arrayMove(state.columns, oldIndex, newIndex);

      console.log("newOrderedColumns: ", newOrderedColumns);

      // 1. Optimistic Update: Cập nhật UI ngay lập tức
      dispatch({
        type: BOARD_ACTIONS.MOVE_COLUMN,
        payload: { newOrderedColumns },
      });

      // 2. Gọi API update ngầm
      try {
        // Chỉ gửi mảng id lên server để tiết kiệm bandwidth
        const newColumnOrderIds = newOrderedColumns.map((c) => c.id);
        await boardApi.updateColumnOrder(state.board.id, newColumnOrderIds);
      } catch (error) {
        // Nếu lỗi -> Cần logic rollback (hoàn tác) state cũ ở đây
        // dispatch({ type: 'SET_BOARD', payload: state.board (cũ) });
        console.error("Lỗi kéo thả cột:", error);
      }
    },
    [state.columns, state.board]
  );

  // 4. Xử lý Kéo thả Task (Move Card) - Logic khó nhất
  // Hàm này nhận vào stateColumns đã được tính toán tạm thời ở component cha (BoardContent)
  // Vì logic tính toán kéo thả khác cột (DragOver) rất phức tạp,
  // nên thường ta tính toán xong ở UI rồi mới quăng vào đây để update State và gọi API.
  const moveCards = useCallback(
    async (dndData) => {
      const { active, over } = dndData;

      // --- A. Kiểm tra an toàn ---
      if (!over || !active || active.id === over.id) return;

      const { id: activeId } = active;
      const { id: overId } = over;

      // --- B. Helper tìm Column ---
      const findColumn = (id) => {
        const column = state.columns.find((c) => c.id === id);
        if (column) return column;
        return state.columns.find((c) =>
          c.tasks?.map((task) => task.id)?.includes(id)
        );
      };

      const activeColumn = findColumn(activeId);
      const overColumn = findColumn(overId);

      if (!activeColumn || !overColumn) return;

      // --- C. Logic tính toán (Clone mảng để không sửa state trực tiếp) ---
      let nextColumns = [...state.columns];
      const activeColIndex = nextColumns.findIndex(
        (c) => c.id === activeColumn.id
      );
      const overColIndex = nextColumns.findIndex((c) => c.id === overColumn.id);

      // C1. Kéo sang cột KHÁC
      if (activeColumn.id !== overColumn.id) {
        const currentCard = activeColumn.tasks.find((c) => c.id === activeId);

        // Xóa ở cột cũ
        nextColumns[activeColIndex] = {
          ...activeColumn,
          tasks: activeColumn.tasks.filter((c) => c.id !== activeId),
          cardOrderIds: activeColumn.cardOrderIds.filter(
            (id) => id !== activeId
          ),
        };

        // Thêm vào cột mới
        const nextOverColumn = { ...overColumn };
        nextOverColumn.tasks = [...overColumn.tasks];
        nextOverColumn.cardOrderIds = [...overColumn.cardOrderIds];

        const overCardIndex = overColumn.tasks.findIndex(
          (c) => c.id === overId
        );
        const newIndex =
          overCardIndex >= 0 ? overCardIndex : nextOverColumn.tasks.length + 1;

        // Cập nhật columnId mới cho Card
        const updatedCard = { ...currentCard, columnId: nextOverColumn.id };

        nextOverColumn.tasks.splice(newIndex, 0, updatedCard);
        nextOverColumn.cardOrderIds.splice(newIndex, 0, updatedCard.id);

        nextColumns[overColIndex] = nextOverColumn;
      }
      // C2. Kéo trong CÙNG cột (Reorder)
      else {
        const oldIndex = activeColumn.tasks.findIndex((c) => c.id === activeId);
        const newIndex = overColumn.tasks.findIndex((c) => c.id === overId);

        if (oldIndex !== newIndex) {
          nextColumns[activeColIndex] = {
            ...activeColumn,
            tasks: arrayMove(activeColumn.tasks, oldIndex, newIndex),
            cardOrderIds: arrayMove(
              activeColumn.cardOrderIds,
              oldIndex,
              newIndex
            ),
          };
        } else {
          return; // Không có gì thay đổi
        }
      }

      // --- D. DISPATCH ACTION ---
      // Đây là phần bạn cần: Gọi dispatch với type MOVE_TASK
      dispatch({
        type: BOARD_ACTIONS.MOVE_TASK,
        payload: nextColumns, // Gửi mảng columns đã tính toán xong
      });

   // 2. Gọi API xử lý ngầm
      // Chúng ta cần biến activeColumn và overColumn (của state CŨ) và nextColumns (state MỚI)
      // để lấy dữ liệu gửi lên Server.

      if (activeColumn.id !== overColumn.id) {
        // === TRƯỜNG HỢP 1: KÉO SANG CỘT KHÁC ===
        
        // Tìm lại cột cũ và cột mới trong mảng nextColumns (state mới) để lấy cardOrderIds mới nhất
        const newPrevColumn = nextColumns.find(c => c.id === activeColumn.id);
        const newNextColumn = nextColumns.find(c => c.id === overColumn.id);
        
        // Gọi API
        await taskColumnApi.moveCardToDifferentColumn({
            currentCardId: activeId,
            prevColumnId: activeColumn.id,
            prevCardOrderIds: newPrevColumn.cardOrderIds, // Mảng order ID mới của cột cũ (đã mất card)
            nextColumnId: overColumn.id,
            nextCardOrderIds: newNextColumn.cardOrderIds  // Mảng order ID mới của cột mới (đã thêm card)
        }).catch(() => {
            // Xử lý lỗi: Rollback state hoặc hiện thông báo
            // dispatch({ type: 'ROLLBACK_STATE', payload: state.columns });
            console.error("Lỗi khi move card sang cột khác");
        });

      } else {
        // === TRƯỜNG HỢP 2: KÉO TRONG CÙNG CỘT ===
        
        // Chỉ gọi API khi vị trí thực sự thay đổi
        const oldIndex = activeColumn.tasks.findIndex((c) => c.id === activeId);
        const newIndex = overColumn.tasks.findIndex((c) => c.id === overId);
        
        if (oldIndex !== newIndex) {
            // Lấy cột đã được update trong nextColumns
            const updatedColumn = nextColumns.find(c => c.id === activeColumn.id);

            // Gọi API
            await taskColumnApi.updateColumnCardOrder(updatedColumn.id, updatedColumn.cardOrderIds)
            .catch(() => {
                console.error("Lỗi khi reorder card");
            });
        }
      }

    },
    [state.columns, dispatch]
  );

  const contextValue = useMemo(
    () => ({
      board: state.board,
      columns: state.columns,
      isLoading: state.isLoading,
      createNewColumn,
      createNewCard,
      moveColumns,
      moveCards,
    }),
    [
      state.board,
      state.columns,
      state.isLoading,
      createNewColumn,
      createNewCard,
      moveColumns,
      moveCards,
    ]
  );
  return (
    <BoardDataContext.Provider value={contextValue}>
      {children}
    </BoardDataContext.Provider>
  );
};
export { BoardDataProvider, BoardDataContext };
