import { useNavigate } from "react-router-dom";

function Home() {

    const navigate = useNavigate();

    return (

        <div
            style={{
                height: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                background: "#f4f8ff"
            }}
        >

            <div
                style={{
                    background: "white",
                    padding: "50px",
                    borderRadius: "15px",
                    width: "420px",
                    textAlign: "center",
                    boxShadow: "0px 5px 20px rgba(0,0,0,0.15)"
                }}
            >

                <h1
                    style={{
                        color: "#1976d2",
                        marginBottom: "10px"
                    }}
                >
                    SkillGap
                </h1>

                <h3
                    style={{
                        color: "#555"
                    }}
                >
                    Intelligent Skill Gap Analysis Platform
                </h3>

                <p
                    style={{
                        color: "gray",
                        marginTop: "20px"
                    }}
                >
                    Analyze your skills and discover the most suitable career role.
                </p>

                <br />

                <button
                    onClick={() => navigate("/register")}
                    style={{
                        width: "100%",
                        padding: "12px",
                        background: "#1976d2",
                        color: "white",
                        border: "none",
                        borderRadius: "8px",
                        cursor: "pointer",
                        fontSize: "16px"
                    }}
                >
                    Register
                </button>

                <br />
                <br />

                <button
                    onClick={() => navigate("/login")}
                    style={{
                        width: "100%",
                        padding: "12px",
                        background: "#43a047",
                        color: "white",
                        border: "none",
                        borderRadius: "8px",
                        cursor: "pointer",
                        fontSize: "16px"
                    }}
                >
                    Login
                </button>

            </div>

        </div>

    );

}

export default Home;