
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import AddSkillForm from "../components/AddSkillForm";
import MySkills from "../components/MySkills";

function ManageSkills() {
    const [refresh, setRefresh] = useState(false);
    const navigate = useNavigate();

    return (
        <div
            style={{
                background: "#f4f8ff",
                minHeight: "100vh",
                paddingBottom: "40px"
            }}
        >
            <Navbar />

            <main
                style={{
                    width: "calc(100% - 32px)",
                    maxWidth: "1100px",
                    margin: "32px auto",
                    background: "#ffffff",
                    padding: "32px",
                    borderRadius: "16px",
                    boxShadow: "0 4px 18px rgba(25, 60, 100, 0.08)",
                    border: "1px solid #e5edf7"
                }}
            >
                <header
                    style={{
                        textAlign: "center",
                        marginBottom: "28px"
                    }}
                >
                    <h1
                        style={{
                            color: "#1976d2",
                            fontSize: "30px",
                            marginBottom: "10px"
                        }}
                    >
                        Manage Skills
                    </h1>

                    <p
                        style={{
                            color: "#64748b",
                            fontSize: "15px",
                            lineHeight: "1.6",
                            margin: 0
                        }}
                    >
                        Add your skills and update your current proficiency level.
                    </p>
                </header>

                <div
                    style={{
                        height: "1px",
                        background: "#e8eef6",
                        marginBottom: "28px"
                    }}
                />

                <section style={{ marginBottom: "32px" }}>
                    <AddSkillForm
                        onSkillAdded={() => setRefresh(prev => !prev)}
                    />
                </section>

                <section style={{ marginBottom: "32px" }}>
                    <MySkills refresh={refresh} />
                </section>

                <footer
                    style={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        gap: "16px",
                        flexWrap: "wrap",
                        borderTop: "1px solid #e8eef6",
                        paddingTop: "24px"
                    }}
                >
                    <button
                        type="button"
                        onClick={() => navigate("/dashboard")}
                        style={{
                            background: "#64748b",
                            color: "#ffffff",
                            border: "1px solid #64748b",
                            padding: "12px 28px",
                            borderRadius: "8px",
                            fontSize: "15px",
                            fontWeight: "600",
                            minWidth: "130px",
                            transition: "background-color 0.2s ease"
                        }}
                        onMouseEnter={e => {
                            e.currentTarget.style.background = "#475569";
                        }}
                        onMouseLeave={e => {
                            e.currentTarget.style.background = "#64748b";
                        }}
                    >
                        Back
                    </button>

                    <button
                        type="button"
                        onClick={() => navigate("/analysis")}
                        style={{
                            background: "#1976d2",
                            color: "#ffffff",
                            border: "1px solid #1976d2",
                            padding: "12px 28px",
                            borderRadius: "8px",
                            fontSize: "15px",
                            fontWeight: "600",
                            minWidth: "150px",
                            transition: "background-color 0.2s ease"
                        }}
                        onMouseEnter={e => {
                            e.currentTarget.style.background = "#125aa0";
                        }}
                        onMouseLeave={e => {
                            e.currentTarget.style.background = "#1976d2";
                        }}
                    >
                        Show Result
                    </button>
                </footer>
            </main>
        </div>
    );
}

export default ManageSkills;
