import HeaderOnly from "@/layouts/HeaderOnly";
import TaskBoard from "@/components/features/TaskBoard";

const BoardPage = () => {
  return (
    <div>
      <HeaderOnly>
        <TaskBoard />
      </HeaderOnly>
    </div>
  );
};
export default BoardPage;
