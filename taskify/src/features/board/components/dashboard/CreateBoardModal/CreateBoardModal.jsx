import classNames from "classnames/bind";
import styles from "./CreateBoardModal.module.scss";
import Input from "@/components/Input";
import Button from "@/components/Button";
import { useBoardList } from "@/features/board/hooks";
import { useState } from "react";

const cx = classNames.bind(styles);
const CreateBoardModal = ({ onClose }) => {
  const { createBoard } = useBoardList();
  const [title, setTitle] = useState("");
  const handleCreateButton = () => {
    createBoard({ title });
    onClose();
  };
  return (
    <div className={cx("modal")}>
      <div className={cx("modal-content")}>
        <span onClick={onClose} className={cx("close-button")}>
          &times;
        </span>
        <h2>Create Board</h2>
        <Input onChange={(e) => setTitle(e.target.value)} label={"Title"} />
        <Button primary onClick={handleCreateButton}>
          Create
        </Button>
      </div>
    </div>
  );
};
export default CreateBoardModal;
