import classNames from "classnames/bind";
import style from "./Button.module.scss";

const cx = classNames.bind(style);
const Button = ({ children, onClick, className, type = "primary" }) => {
  return (
    <button className={cx(`button-${type}`, className)} onClick={onClick}>
      {children}
    </button>
  );
};
export default Button;
