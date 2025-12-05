import { useState } from "react";
import classNames from "classnames/bind";
import styles from "./Login.module.scss";

import Input from "../../../common/Input";
import Button from "../../../common/Button";
import { useAuth } from "../../../../hooks/useAuth.jsx";
import { useLocation, useNavigate, Link } from "react-router-dom";

const cx = classNames.bind(styles);

/**
 * @component Login
 * @description A form for user login.
 * @returns {JSX.Element} The Login component.
 */
const Login = () => {
  const { login, error } = useAuth();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/";

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await login(username, password);
      navigate(from, { replace: true });
    } catch (err) {
      console.error("Error during login:", err);
    }
  };

  return (
    <div className={cx("authContainer")}>
      <h2 className={cx("title")}>Welcome Back</h2>

      {error && <div className={cx("error")}>{error}</div>}

      <form onSubmit={handleSubmit}>
        <div className={cx("inputGroup")}>
          <label>Username</label>
          <Input
            value={username}
            name={"username"}
            autoComplete="username"
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div className={cx("inputGroup")}>
          <label>Password</label>
          <Input
            type="password"
            name={"password"}
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <Button
          type="submit"
          primary
          style={{ width: "100%" }}
        >
          Login
        </Button>
      </form>

      <p className={cx("toggleText")}>
        Don't have an account?
        <Link to="/register" className={cx("link")}>Sign Up</Link>
      </p>
    </div>
  );
};
export default Login;
