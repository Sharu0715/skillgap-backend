
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import Navbar from "../components/Navbar";

function Dashboard() {
    const [student, setStudent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const navigate = useNavigate();
    const studentId = localStorage.getItem("studentId");

    useEffect(() => {
        const fetchStudent = async () => {
            if (!studentId) {
                setError("Student session not found. Please log in again.");
                setLoading(false);
                return;
            }

            try {
                const response = await api.get(
                    `/api/students/${studentId}`
                );
                setStudent(response.data);
            } catch (err) {
                console.error("Failed to fetch student:", err);
                setError(
                    "Unable to load your dashboard. Please try again."
                );
            } finally {
                setLoading(false);
            }
        };

        fetchStudent();
    }, [studentId]);

    return (
        <div style={styles.page}>
            <Navbar />

            <main style={styles.main}>
                {loading ? (
                    <div style={styles.messageCard}>
                        <p>Loading your dashboard...</p>
                    </div>
                ) : error ? (
                    <div style={styles.messageCard}>
                        <h2 style={styles.errorTitle}>
                            Dashboard unavailable
                        </h2>

                        <p style={styles.message}>
                            {error}
                        </p>

                        {!studentId && (
                            <button
                                style={styles.button}
                                onClick={() => navigate("/login")}
                            >
                                Go to Login
                            </button>
                        )}

                        {studentId && (
                            <button
                                style={styles.button}
                                onClick={() => window.location.reload()}
                            >
                                Try Again
                            </button>
                        )}
                    </div>
                ) : (
                    <>
                        <section style={styles.welcome}>
                            <p style={styles.eyebrow}>
                                YOUR CAREER JOURNEY
                            </p>

                            <h1 style={styles.heading}>
                                Welcome, {student?.name}!
                            </h1>

                            <p style={styles.description}>
                                Build your skills, identify skill gaps,
                                and discover career roles that match
                                your current abilities.
                            </p>

                            <button
                                onClick={() =>
                                    navigate("/manage-skills")
                                }
                                style={styles.button}
                            >
                                Manage My Skills
                            </button>
                        </section>

                        <section style={styles.infoSection}>
                            <h2 style={styles.sectionTitle}>
                                Get Started
                            </h2>

                            <div style={styles.cardGrid}>
                                <article style={styles.card}>
                                    <div style={styles.cardIcon}>
                                        01
                                    </div>

                                    <h3>Manage Skills</h3>

                                    <p style={styles.cardText}>
                                        Add your skills and update
                                        your current proficiency levels.
                                    </p>

                                    <button
                                        style={styles.textButton}
                                        onClick={() =>
                                            navigate("/manage-skills")
                                        }
                                    >
                                        Update Skills →
                                    </button>
                                </article>

                                <article style={styles.card}>
                                    <div style={styles.cardIcon}>
                                        02
                                    </div>

                                    <h3>Find Your Career Match</h3>

                                    <p style={styles.cardText}>
                                        Use your skill profile to
                                        understand suitable job roles
                                        and identify areas to improve.
                                    </p>
                                </article>
                            </div>
                        </section>
                    </>
                )}
            </main>
        </div>
    );
}

const styles = {
    page: {
        minHeight: "100vh",
        background: "#f4f7fc",
        color: "#172b4d",
    },

    main: {
        width: "min(1100px, 92%)",
        margin: "0 auto",
        padding: "48px 0",
    },

    welcome: {
        background: "linear-gradient(135deg, #e5efff, #ffffff)",
        padding: "42px",
        borderRadius: "18px",
        border: "1px solid #dce7f7",
        boxShadow: "0 6px 24px rgba(35, 70, 120, 0.06)",
        textAlign: "center",
    },

    eyebrow: {
        color: "#1976d2",
        fontSize: "12px",
        fontWeight: "700",
        letterSpacing: "2px",
        marginBottom: "14px",
    },

    heading: {
        color: "#163d72",
        fontSize: "clamp(28px, 4vw, 38px)",
        margin: "14px 0",
    },

    description: {
        color: "#53657d",
        fontSize: "16px",
        lineHeight: 1.7,
        maxWidth: "650px",
        margin: "0 auto 26px",
        textAlign: "center",
    },

    button: {
        padding: "12px 22px",
        background: "#1976d2",
        color: "#ffffff",
        border: "none",
        borderRadius: "8px",
        cursor: "pointer",
        fontSize: "15px",
        fontWeight: "600",
    },

    infoSection: {
        marginTop: "38px",
    },

    sectionTitle: {
        fontSize: "23px",
        color: "#163d72",
        marginBottom: "20px",
    },

    cardGrid: {
        display: "grid",
        gridTemplateColumns: "repeat(auto-fit, minmax(240px, 1fr))",
        gap: "22px",
    },

    card: {
        background: "#ffffff",
        padding: "26px",
        borderRadius: "14px",
        border: "1px solid #e2eaf5",
        boxShadow: "0 4px 16px rgba(35, 70, 120, 0.04)",
    },

    cardIcon: {
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        width: "42px",
        height: "42px",
        borderRadius: "10px",
        background: "#e8f1ff",
        color: "#1976d2",
        fontWeight: "700",
    },

    cardText: {
        color: "#64748b",
        lineHeight: 1.7,
        fontSize: "14px",
    },

    textButton: {
        marginTop: "10px",
        padding: "0",
        border: "none",
        background: "transparent",
        color: "#1976d2",
        fontWeight: "600",
        cursor: "pointer",
        fontSize: "14px",
    },

    messageCard: {
        background: "#ffffff",
        padding: "36px",
        borderRadius: "14px",
        textAlign: "center",
        boxShadow: "0 4px 18px rgba(35, 70, 120, 0.08)",
    },

    message: {
        color: "#64748b",
        lineHeight: 1.6,
    },

    errorTitle: {
        color: "#b42318",
    },
};

export default Dashboard;
