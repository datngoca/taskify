import { ACTION_TYPES } from "./actionType";

export const initialState = {
    tasks: [],
    task: null,
    loading: false,
    error: null
};

export const taskReducer = (state = initialState, action) => {
    switch (action.type) {
        case ACTION_TYPES.FETCH_TASKS:
            return {
                ...state,
                tasks: [],
                loading: true,
                error: null
            }
        case ACTION_TYPES.FETCH_TASKS_SUCCESS:
            return {
                ...state,
                tasks: action.payload,
                loading: false,
                error: null
            }
        case ACTION_TYPES.FETCH_TASK_FAIL:
            return {
                ...state,
                tasks: [],
                loading: false,
                error: action.payload
            }

        case ACTION_TYPES.CREATE_TASK:
        return {
            ...state,
            // Thêm task mới (tempId) vào cuối danh sách
            tasks: [...state.tasks, action.payload], 
            loading: false, // Không cần loading toàn trang, vì task đã hiện rồi
            error: null,
        };
        case ACTION_TYPES.CREATE_TASK_SUCCESS:
        return {
            ...state,
            tasks: state.tasks.map((task) => {
            // Tìm task có tempId và thay bằng realTask
            if (task.id === action.payload.tempId) {
                return action.payload.realTask;
            }
            return task;
            }),
            error: null,
        };
        case ACTION_TYPES.CREATE_TASK_FAIL:
            return {
                ...state,
                // Lọc bỏ task có tempId vì tạo thất bại
                tasks: state.tasks.filter((task) => task.id !== action.payload.tempId),
                error: action.payload.error,
            };
            
        case ACTION_TYPES.UPDATE_TASK:
            return {
                ...state,
                 tasks: state.tasks.map((task) =>
                    task.id === action.payload.id ? action.payload : task
                ),
                loading: true,
                error: null
            };
        case ACTION_TYPES.UPDATE_TASK_SUCCESS:
            return {
                ...state,
                loading: false,
                error: null
            };
        case ACTION_TYPES.UPDATE_TASK_FAIL:
            return {
                ...state,
                tasks: state.tasks.map((task) =>
                    task.id === action.payload.task.id
                        ? action.payload.task
                        : task
                    ),
                loading: false,
                error: action.payload
            }
        
        case ACTION_TYPES.DELETE_TASK:
            return {
                ...state,
                tasks: state.tasks.filter((task) => task.id !== action.payload),
                loading: true,
                error: null
            }
        case ACTION_TYPES.DELETE_TASK_SUCCESS:
            return {
                ...state,
                loading: false,
                error: null
            };
        case ACTION_TYPES.DELETE_TASK_FAIL:{  
            // Tạo bản sao mảng hiện tại để thao tác
            const tasksRestored = [...state.tasks];
            
            // Chèn lại task cũ vào đúng vị trí cũ (index)
            // payload gồm: { task: object, index: number, error: string }
            if (action.payload.task) {
                tasksRestored.splice(action.payload.index, 0, action.payload.task);
            }
            return {
                ...state,
                task:tasksRestored,
                loading: false,
                error: action.payload
            }}
    case ACTION_TYPES.DRAG_TASK_OPTIMISTIC:
      return {
        ...state,
        tasks: state.tasks.map((task) =>
          task.id === action.payload.id ? action.payload : task
        ),
        loading: true,
        error: null,
      };
    case ACTION_TYPES.DRAG_TASK_SUCCESS:
      return {
        ...state,
        loading: false,
        error: null,
      };
    case ACTION_TYPES.DRAG_TASK_FAIL:
      return {
        ...state,
        tasks: action.payload.tasks, // Revert to original tasks
        loading: false,
        error: action.payload.error,
      };

        default:
            return state;
    }
};

