import { useState, useEffect } from "react";
import {boardApi} from "../api/boardApi";

const useBoardList = () => {
  const [boards, setBoards] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchBoards = async () => {
    try {
      setIsLoading(true);
      const response = await boardApi.fetchBoardData();
      setBoards(response);
    } catch (err) {
      setError(err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchBoards();
  }, []);

  const createNewBoard = async (board) => {
    try {
      const newCardData = await boardApi.createNewBoard(board);
      setBoards((prev) => [...prev, newCardData]);
    } catch (error) {
      console.error("Lỗi tạo board:", error);
    }
  };
  return { boards, isLoading, error, refetch: fetchBoards, createBoard: createNewBoard };
};
export { useBoardList };
