
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function RegisterStudent() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleRegister = async () => {
        // Name validation
        if (name.trim() === "") {
            alert("Please enter your name.");
            return;
        }

        // Email validation
        const emailRegex =
            /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

        if (!emailRegex.test(email.trim())) {
            alert("Please enter a valid email address.");
            return;
        }

        // Check whether both password fields are filled
        if (password === "" || confirmPassword === "") {
            alert("Please enter and confirm your password.");
            return;
        }

        // First check password matching
        if (password !== confirmPassword) {
            alert("Password and Confirm Password do not match.");
            return;
        }

        // Then validate password format
        const passwordRegex =
            /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/;

        if (!passwordRegex.test(password)) {
            alert(
                "Password must contain at least 8 characters, one uppercase letter, one number and one special character."
            );
            return;
        }

        const studentData = {
            name: name.trim(),
            email: email.trim(),
            password: password
        };

        try {
            setLoading(true);

            await axios.post(
                "http://localhost:8080/api/students",
                studentData
            );

            alert("Student Registered Successfully!");
            navigate("/login");

        } catch (error) {
            console.error("Registration error:", error);

            if (error.response) {
                const message = error.response.data;

                if (typeof message === "string") {
                    alert(message);
                } else if (message?.message) {
                    alert(message.message);
                } else {
                    alert(
                        `Registration failed. Server returned status ${error.response.status}.`
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
                        color: "#1976d2",
                        marginBottom: "25px"
                    }}
                >
                    Student Registration
                </h2>

                {/* Name */}
                <input
                    type="text"
                    placeholder="Enter Name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    style={{
                        ...inputStyle,
                        marginBottom: "15px"
                    }}
                />

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

                {/* Password */}
                <div
                    style={{
                        position: "relative",
                        marginBottom: "15px"
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
                            showPassword ? "Hide password" : "Show password"
                        }
                        style={toggleButtonStyle}
                    >
                        {showPassword ? "Hide" : "Show"}
                    </button>
                </div>

                {/* Confirm Password */}
                <div
                    style={{
                        position: "relative",
                        marginBottom: "20px"
                    }}
                >
                    <input
                        type={showConfirmPassword ? "text" : "password"}
                        placeholder="Confirm Password"
                        value={confirmPassword}
                        onChange={(e) =>
                            setConfirmPassword(e.target.value)
                        }
                        style={{
                            ...inputStyle,
                            paddingRight: "65px"
                        }}
                    />

                    <button
                        type="button"
                        onClick={() =>
                            setShowConfirmPassword((previous) => !previous)
                        }
                        aria-label={
                            showConfirmPassword
                                ? "Hide confirm password"
                                : "Show confirm password"
                        }
                        style={toggleButtonStyle}
                    >
                        {showConfirmPassword ? "Hide" : "Show"}
                    </button>
                </div>

                {/* Register Button */}
                <button
                    type="button"
                    onClick={handleRegister}
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
                    {loading ? "Registering..." : "Register"}
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
            </div>
        </div>
    );
}

export default RegisterStudent;
