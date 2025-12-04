import { useState } from "react";
import classNames from "classnames/bind";
import styles from "./AuthForm.module.scss";

import Input from "../../common/Input";
import Button from "../../common/Button";
import { useAuth } from "../../../hooks/useAuth.jsx";
import { useLocation, useNavigate } from "react-router-dom";
const cx = classNames.bind(styles);
const Login = () => {
  const { login, register, error } = useAuth();
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/";

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isLogin) {
        await login(username, password);
        navigate(from, { replace: true });
      } else {
        await register(username, password);
      }
    } catch (err) {
      console.error("Error during authentication:", err);
    }
  };
  return (
    <div className={cx("authContainer")}>
      <h2 className={cx("title")}>
        {isLogin ? "Welcome Back" : "Create Account"}
      </h2>

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
            autoComplete={isLogin ? "current-password" : "new-password"}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <Button
          onClick={handleSubmit}
          type="submit"
          primary
          style={{ width: "100%" }}
        >
          {isLogin ? "Login" : "Sign Up"}
        </Button>
      </form>

      <p className={cx("toggleText")}>
        {isLogin ? "Don't have an account?" : "Already have an account?"}
        <span onClick={() => setIsLogin(!isLogin)}>
          {isLogin ? "Sign Up" : "Login"}
        </span>
      </p>
    </div>
  );
};
export default Login;
