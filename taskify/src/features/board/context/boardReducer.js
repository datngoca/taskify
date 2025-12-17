import { mapOrder } from "../utils/sorts";
import { BOARD_ACTIONS } from "./actionTypes";
const initialState = {
  board: null,
  columns: [],
  isLoading: true,
};

const boardReducer = (state, action) => {
  switch (action.type) {
    case BOARD_ACTIONS.SET_BOARD: {
      const board = action.payload;

      // Sắp xếp columns theo columnOrderIds
      const sortedColumns = mapOrder(board.columns, board.columnOrderIds, "id");

      return {
        ...state,
        board: board,
        columns: sortedColumns,
        isLoading: false,
      };
    }

    case BOARD_ACTIONS.ADD_COLUMN: {
      const newColumn = action.payload;
      return {
        ...state,
        columns: [...state.columns, newColumn],
        board: {
          ...state.board,
          columnOrderIds: [...state.board.columnOrderIds, newColumn.id],
        },
      };
    }

    case BOARD_ACTIONS.ADD_TASK: {
      const { columnId, newTask } = action.payload;

      const columnToUpdate = state.columns.find((c) => c.id === columnId);
      if (!columnToUpdate) return state;

      // Clone column cũ và cập nhật cards
      const updatedColumn = {
        ...columnToUpdate,
        tasks: [...columnToUpdate.tasks, newTask],
        cardOrderIds: [...columnToUpdate.cardOrderIds, newTask.id],
      };

      // Cập nhật lại mảng columns
      const newColumns = state.columns.map((c) =>
        c.id === columnId ? updatedColumn : c
      );
      return { ...state, columns: newColumns };
    }

    case BOARD_ACTIONS.MOVE_COLUMN: {
      // payload chứa mảng columns đã được dnd-kit tính toán vị trí mới
      const { newOrderedColumns } = action.payload;
      return {
        ...state,
        columns: newOrderedColumns,
        board: {
          ...state.board,
          columnOrderIds: newOrderedColumns.map((c) => c.id),
        },
      };
    }

    case BOARD_ACTIONS.MOVE_TASK: {
      return {
        ...state,
        columns: action.payload,
        board: {
          ...state.board,
          columns: action.payload,
        },
      };
    }

    default:
      return state;
  }
};
export { initialState, boardReducer };
