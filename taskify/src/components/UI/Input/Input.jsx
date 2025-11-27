import classNames from "classnames/bind";
import style from "./Input.module.scss";

const cx = classNames.bind(style);

const Input = ({ label, error, className, ...props }) => {
  return (
    <div className={cx("container", className)}>
      {label && <label className={cx("label")}>{label}</label>}

      <input
        // Logic: Always have 'inputField'. If error exists, add 'hasError'.
        className={cx("inputField", {
          hasError: props.error,
        })}
        {...props}
      />

      {error && <span className={cx("errorMsg")}>{error}</span>}
    </div>
  );
};

export default Input;
