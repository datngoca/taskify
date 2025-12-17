import React, { memo } from 'react';
import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import classNames from 'classnames/bind';
import styles from './TaskCard.module.scss';
import Button from '@/components/Button';

const cx = classNames.bind(styles);

const TaskCard = ({ task, isOverlay }) => {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging
  } = useSortable({
    id: task.id,
    data: { ...task, type: 'TASK' }
  });

  const style = {
    transform: CSS.Translate.toString(transform),
    transition,
  };

  return (
    <div
      ref={setNodeRef}
      style={style}
      {...attributes}
      {...listeners}
      className={cx('taskCard', {
        isDragging: isDragging,
        dragOverlay: isOverlay // Style riêng khi đang bay (Overlay)
      })}
    >
      {/* Cover Image nếu có */}
      {task.cover && (
        <img src={task.cover} alt="cover" className={cx('cover')} />
      )}
      
      <div className={cx('title')}>{task.title}</div>

      <Button danger className={cx("actions")}>Delete</Button>
    </div>
  );
};

export default memo(TaskCard);