import { useContext } from "react";
import { BoardDataContext } from "../context/BoardDataContext";

const useBoardData = () => {
  const context = useContext(BoardDataContext);
  if (!context) {
    throw new Error("useBoard must be used within an AuthProvider");
  }
  return context;
};

export { useBoardData };
