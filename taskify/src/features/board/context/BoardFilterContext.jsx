import { createContext } from "react";

const BoardFilterContext = createContext();
const BoardFilterProvider = ({ children }) => {
  return <BoardFilterContext.Provider>{children}</BoardFilterContext.Provider>;
};
export { BoardFilterProvider, BoardFilterContext };
