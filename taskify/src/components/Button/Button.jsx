
import classNames from "classnames/bind";
import { Link } from "react-router-dom";
import styles from "./Button.module.scss";

const cx = classNames.bind(styles);

function Button({
  to,
  href,
  primary = false,
  secondary = false,
  danger = false,
  active = false,
  disable = false,
  // Giữ lại các props kích thước nếu bạn định thêm CSS sau này
  small = false,
  large = false,
  children,
  className,
  leftIcon,
  rightIcon,
  onClick,
  ...passProps
}) {
  let Comp = "button";
  const props = {
    onClick,
    ...passProps,
  };

  // Xử lý logic disable (vô hiệu hóa sự kiện)
  if (disable) {
    Object.keys(props).forEach((key) => {
      if (key.startsWith("on") && typeof props[key] === "function") {
        delete props[key];
      }
    });
  }

  // Xử lý loại thẻ (Link, a, button)
  if (to) {
    props.to = to;
    Comp = Link;
  } else if (href) {
    props.href = href;
    Comp = "a";
  }

  // Tạo classes dựa trên props và SCSS
  const classes = cx("btn", {
    [className]: className,
    primary, // Map với .primary trong SCSS
    secondary, // Map với .secondary trong SCSS
    danger, // Map với .danger trong SCSS
    active, // Map với .secondary.active
    disable, // Thêm class disable nếu cần style (dù SCSS chưa có)
    small,
    large,
  });

  return (
    <Comp className={classes} {...props}>
      {leftIcon && <span className={cx("icon")}>{leftIcon}</span>}
      <span className={cx("title")}>{children}</span>
      {rightIcon && <span className={cx("icon")}>{rightIcon}</span>}
    </Comp>
  );
}

export default Button;
