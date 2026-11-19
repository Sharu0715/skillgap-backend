import { useNavigate } from "react-router-dom";

function Navbar() {

    const navigate = useNavigate();

    const name = localStorage.getItem("name");

    const logout = () => {

        localStorage.removeItem("token");
        localStorage.removeItem("studentId");
        localStorage.removeItem("name");

        navigate("/");

    };

    return (

        <div
            style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                padding: "15px 30px",
                backgroundColor: "#1976d2",
                color: "white"
            }}
        >

            <h2
                style={{ cursor: "pointer" }}
                onClick={() => navigate("/dashboard")}
            >
                SkillGap
            </h2>

            <div>

                <span>
                    Welcome, {name}
                </span>




                <button
                    style={{
                        marginLeft: "10px",
                        cursor: "pointer"
                    }}
                    onClick={logout}
                >
                    Logout
                </button>

            </div>

        </div>

    );

}

export default Navbar;