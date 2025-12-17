import { useParams } from "react-router-dom";
// import classNames from "classnames/bind";

// import styles from "./WorkSpace.module.scss";
import Button from "@/components/Button";
import { FaArrowLeft } from "react-icons/fa";

import { BoardDataProvider } from "@/features/board/context";
import Board from "./Board";
// const cx = classNames.bind(styles);

const WorkSpace = () => {
  const { id } = useParams();
  return (
    <BoardDataProvider boardId={id}>
      <Board />
    </BoardDataProvider>
  );
};

export default WorkSpace;
