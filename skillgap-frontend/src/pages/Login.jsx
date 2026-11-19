
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [showPassword, setShowPassword] = useState(false);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        if (email.trim() === "") {
            alert("Email is required.");
            return;
        }

        const emailPattern =
            /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

        if (!emailPattern.test(email.trim())) {
            alert("Please enter a valid email address.");
            return;
        }

        if (password === "") {
            alert("Password is required.");
            return;
        }

        const loginData = {
            email: email.trim(),
            password: password
        };

        try {
            setLoading(true);

            const response = await axios.post(
                "http://localhost:8080/auth/login",
                loginData
            );

            const token = response.data.token;

            if (!token) {
                alert("Login failed: Authentication token was not received.");
                return;
            }

            const decodedToken = jwtDecode(token);

            localStorage.setItem("token", token);

            if (response.data.studentId != null) {
                localStorage.setItem(
                    "studentId",
                    response.data.studentId
                );
            }

            if (response.data.name) {
                localStorage.setItem(
                    "name",
                    response.data.name
                );
            }

            console.log("Decoded JWT:", decodedToken);

            alert("Login Successful!");

            navigate("/dashboard");

        } catch (error) {
            console.error("Login error:", error);

            if (error.response) {
                const status = error.response.status;
                const message = error.response.data;

                if (status === 401 || status === 403) {
                    alert("Invalid email or password. Please try again.");
                } else if (status === 404) {
                    alert("Login endpoint not found. Please check the backend API.");
                } else if (typeof message === "string") {
                    alert(message);
                } else if (message?.message) {
                    alert(message.message);
                } else {
                    alert(
                        `Login failed. Server returned status ${status}.`
                    );
                }
            } else if (error.request) {
                alert(
                    "Cannot connect to the server. Please check whether the backend is running."
                );
            } else {
                alert("Something went wrong. Please try again.");
            }

        } finally {
            setLoading(false);
        }
    };

    const inputStyle = {
        boxSizing: "border-box",
        width: "100%",
        padding: "10px",
        border: "1px solid #ccc",
        borderRadius: "6px",
        fontSize: "14px"
    };

    const toggleButtonStyle = {
        position: "absolute",
        right: "8px",
        top: "50%",
        transform: "translateY(-50%)",
        border: "none",
        background: "transparent",
        cursor: "pointer",
        color: "#1976d2",
        fontWeight: "600"
    };

    return (
        <div
            style={{
                minHeight: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                background: "#f4f8ff",
                padding: "20px"
            }}
        >
            <div
                style={{
                    width: "420px",
                    maxWidth: "100%",
                    background: "white",
                    padding: "35px",
                    borderRadius: "15px",
                    boxShadow: "0 5px 15px rgba(0,0,0,0.2)"
                }}
            >
                <h2
                    style={{
                        textAlign: "center",
                        color: "#1976d2"
                    }}
                >
                    Student Login
                </h2>

                <br />

                <form onSubmit={handleLogin}>
                    {/* Email */}
                    <input
                        type="email"
                        placeholder="Enter Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        style={{
                            ...inputStyle,
                            marginBottom: "15px"
                        }}
                    />

                    {/* Password with Show/Hide */}
                    <div
                        style={{
                            position: "relative",
                            marginBottom: "20px"
                        }}
                    >
                        <input
                            type={showPassword ? "text" : "password"}
                            placeholder="Enter Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            style={{
                                ...inputStyle,
                                paddingRight: "65px"
                            }}
                        />

                        <button
                            type="button"
                            onClick={() =>
                                setShowPassword((previous) => !previous)
                            }
                            aria-label={
                                showPassword
                                    ? "Hide password"
                                    : "Show password"
                            }
                            style={toggleButtonStyle}
                        >
                            {showPassword ? "Hide" : "Show"}
                        </button>
                    </div>

                    {/* Login Button */}
                    <button
                        type="submit"
                        disabled={loading}
                        style={{
                            width: "100%",
                            padding: "12px",
                            background: loading ? "#90caf9" : "#1976d2",
                            color: "white",
                            border: "none",
                            borderRadius: "8px",
                            cursor: loading ? "not-allowed" : "pointer",
                            fontSize: "16px"
                        }}
                    >
                        {loading ? "Logging in..." : "Login"}
                    </button>

                    <br />
                    <br />

                    {/* Back to Home */}
                    <button
                        type="button"
                        onClick={() => navigate("/")}
                        style={{
                            width: "100%",
                            padding: "12px",
                            background: "#757575",
                            color: "white",
                            border: "none",
                            borderRadius: "8px",
                            cursor: "pointer",
                            fontSize: "16px"
                        }}
                    >
                        Back to Home
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Login;
