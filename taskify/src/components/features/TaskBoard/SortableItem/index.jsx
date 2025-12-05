import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";

const SortableItem = (props) => {
  // Hook này cung cấp các thuộc tính cần thiết để element có thể kéo thả
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging, // Trạng thái: đang kéo hay không
  } = useSortable({ id: props.id });

  // Style để task di chuyển mượt mà theo chuột
  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
    // Nếu đang kéo thì làm mờ item gốc đi 1 chút để dễ nhìn
    opacity: isDragging ? 0.3 : 1,
    cursor: "grab",
  };

  return (
    <div ref={setNodeRef} style={style} {...attributes} {...listeners}>
      {/* Render nội dung Task được truyền từ bên ngoài vào */}
      {props.children}
    </div>
  );
};
export default SortableItem;
