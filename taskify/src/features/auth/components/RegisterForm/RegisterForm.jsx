import { useState } from "react";
import classNames from "classnames/bind";
import styles from "./RegisterForm.module.scss";

import Input from "@/components/Input";
import Button from "@/components/Button";
import { useAuth } from "@/features/auth/hooks";
import { useNavigate, Link } from "react-router-dom";

const cx = classNames.bind(styles);

/**
 * @component Register
 * @description A form for user registration.
 * @returns {JSX.Element} The Register component.
 */
const Register = () => {
  const { register, error } = useAuth();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await register(username, password);
      // Optional: navigate to login after successful registration
      navigate('/login');
    } catch (err) {
      console.error("Error during registration:", err);
    }
  };

  return (
    <div className={cx("authContainer")}>
      <h2 className={cx("title")}>Create Account</h2>

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
            autoComplete="new-password"
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
          Sign Up
        </Button>
      </form>

      <p className={cx("toggleText")}>
        Already have an account?
        <Link to="/login" className={cx("link")}>Login</Link>
      </p>
    </div>
  );
};
export default Register;
