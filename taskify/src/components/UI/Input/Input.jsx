import classNames from "classnames/bind";
import style from "./Input.module.scss";

const cx = classNames.bind(style);

const Input = ({ type, placeholder, defaultValue, onChange, className }) => {
  return (
    <input
      className={className}
      type={type}
      placeholder={placeholder}
      defaultValue={defaultValue}
      onChange={onChange}
    />
  );
};
export default Input;
