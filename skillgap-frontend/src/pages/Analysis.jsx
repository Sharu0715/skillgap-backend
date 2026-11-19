
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";
import {
    analyzeSkills,
    getAiRecommendation
} from "../services/analysisService";
import Navbar from "../components/Navbar";



function Analysis() {
    const [analysis, setAnalysis] = useState([]);
    const [aiRecommendation, setAiRecommendation] = useState("");
    const [showAiRecommendation, setShowAiRecommendation] = useState(false);
    const [aiLoading, setAiLoading] = useState(false);
    const [loading, setLoading] = useState(true);
    const [analysisError, setAnalysisError] = useState(false);

    const studentId = localStorage.getItem("studentId");
    const navigate = useNavigate();

    useEffect(() => {
        loadAnalysis();
    }, []);

    const loadAnalysis = async () => {
        try {
            setLoading(true);
            setAnalysisError(false);

            if (!studentId) {
                setAnalysisError(true);
                return;
            }

            const data = await analyzeSkills(studentId);
            setAnalysis(Array.isArray(data) ? data : []);
        } catch (error) {
               if (error.response?.status === 401) {
                   return;
               }

               setAnalysisError(true);
           } finally {
            setLoading(false);
        }
    };

    const loadAiRecommendation = async () => {
        try {
            setAiLoading(true);
            setShowAiRecommendation(true);
            setAiRecommendation("");

            const data = await getAiRecommendation(studentId);

            const recommendation =
                typeof data === "string"
                    ? data
                    : data?.recommendation ||
                      data?.message ||
                      "No recommendation was returned.";

            // Display the AI response as ordinary text, not Markdown bold.
            setAiRecommendation(
                String(recommendation)
                    .replace(/\*/g, "")
                    .trim()
            );
        } catch (error) {
            console.error("AI Recommendation failed:", error);
            setAiRecommendation(
                "We couldn't generate your recommendation. Please try again."
            );
        } finally {
            setAiLoading(false);
        }
    };


    const downloadReport = () => {
        if (!analysis.length) {
            alert("No analysis result available to download.");
            return;
        }

        const doc = new jsPDF();
        const reportDate = new Date().toLocaleDateString("en-IN");
        const margin = 14;
        const contentWidth = doc.internal.pageSize.getWidth() - margin * 2;

        doc.setFontSize(20);
        doc.text("SkillGap Analysis Report", margin, 20);

        doc.setFontSize(10);
        doc.text(`Student ID: ${studentId || "N/A"}`, margin, 30);
        doc.text(`Report Date: ${reportDate}`, margin, 37);

        const best = analysis[0];

        doc.setFontSize(14);
        doc.text("Best Matched Role", margin, 49);

        doc.setFontSize(11);
        doc.text(`Role: ${best.roleName || "N/A"}`, margin, 58);
        doc.text(
            `Match: ${Number(best.matchPercentage).toFixed(2)}%`,
            margin,
            65
        );
        doc.text(
            `Eligibility: ${best.eligible ? "Eligible" : "Not Eligible"}`,
            margin,
            72
        );

        autoTable(doc, {
            startY: 83,
            head: [["Job Role", "Skill Match", "Eligibility"]],
            body: analysis.map((role) => [
                role.roleName || "N/A",
                `${Number(role.matchPercentage).toFixed(2)}%`,
                role.eligible ? "Eligible" : "Not Eligible"
            ]),
            margin: { left: margin, right: margin },
            tableWidth: contentWidth,
            styles: {
                fontSize: 10,
                cellPadding: 4,
                overflow: "linebreak"
            },
            headStyles: {
                fillColor: [36, 93, 168]
            }
        });

        let finalY = (doc.lastAutoTable?.finalY || 83) + 12;

        if (aiRecommendation.trim()) {
            const recommendationLines = doc.splitTextToSize(
                aiRecommendation,
                contentWidth
            );
            const pageHeight = doc.internal.pageSize.getHeight();
            const headingSpace = 8;
            const lineHeight = 5;
            const neededHeight =
                headingSpace + recommendationLines.length * lineHeight + 8;

            if (finalY + neededHeight > pageHeight - margin) {
                doc.addPage();
                finalY = margin;
            }

            doc.setFontSize(14);
            doc.text("AI Career Recommendation", margin, finalY);

            doc.setFontSize(10);
            doc.text(recommendationLines, margin, finalY + headingSpace);
        }

        doc.save(`SkillGap_Report_${studentId || "Student"}.pdf`);
    };

    const bestRole = analysis.length > 0 ? analysis[0] : null;

    const styles = {
        page: {
            minHeight: "100vh",
            background: "#f3f6fb",
            color: "#1e293b",
            paddingBottom: "48px",
            fontFamily: "Inter, Segoe UI, Arial, sans-serif"
        },
        container: {
            width: "min(1080px, calc(100% - 36px))",
            margin: "32px auto",
            boxSizing: "border-box"
        },
        pageHeader: {
            marginBottom: "26px"
        },
        eyebrow: {
            color: "#5274a3",
            fontSize: "12px",
            fontWeight: "700",
            letterSpacing: "1.3px",
            textTransform: "uppercase",
            margin: "0 0 8px"
        },
        title: {
            fontSize: "clamp(26px, 4vw, 32px)",
            fontWeight: "700",
            letterSpacing: "-0.6px",
            lineHeight: "1.25",
            color: "#17365d",
            margin: "0 0 9px"
        },
        subtitle: {
            color: "#64748b",
            fontSize: "15px",
            lineHeight: "1.7",
            margin: 0
        },
        panel: {
            background: "#ffffff",
            border: "1px solid #e1e8f0",
            borderRadius: "14px",
            boxShadow: "0 4px 16px rgba(15, 35, 65, 0.04)",
            padding: "clamp(18px, 3vw, 28px)",
            marginBottom: "26px"
        },
        bestCard: {
            position: "relative",
            overflow: "hidden",
            background: "linear-gradient(115deg, #ffffff 0%, #f1f6ff 100%)",
            border: "1px solid #c9d9ee",
            borderLeft: "5px solid #285da8",
            borderRadius: "14px",
            padding: "clamp(20px, 3vw, 30px)",
            marginBottom: "28px",
            boxShadow: "0 5px 18px rgba(31, 76, 135, 0.06)"
        },
        label: {
            color: "#61748d",
            fontSize: "12px",
            fontWeight: "700",
            textTransform: "uppercase",
            letterSpacing: "1.1px",
            margin: "0 0 10px"
        },
        roleName: {
            color: "#17365d",
            fontSize: "clamp(23px, 4vw, 29px)",
            fontWeight: "700",
            letterSpacing: "-0.3px",
            lineHeight: "1.4",
            margin: "0 0 24px",
            overflowWrap: "anywhere"
        },
        metrics: {
            display: "flex",
            alignItems: "center",
            flexWrap: "wrap",
            gap: "28px"
        },
        metric: {
            minWidth: "145px"
        },
        metricLabel: {
            color: "#64748b",
            fontSize: "13px",
            display: "block",
            marginBottom: "7px"
        },
        metricValue: {
            color: "#1e293b",
            fontSize: "20px",
            fontWeight: "700"
        },
        progressTrack: {
            height: "7px",
            width: "min(100%, 300px)",
            background: "#dce6f3",
            borderRadius: "20px",
            overflow: "hidden",
            marginTop: "11px"
        },
        sectionTitle: {
            color: "#1e3a5f",
            fontSize: "19px",
            fontWeight: "700",
            lineHeight: "1.4",
            margin: "0 0 7px"
        },
        sectionDescription: {
            color: "#64748b",
            fontSize: "14px",
            lineHeight: "1.65",
            margin: "0 0 20px"
        },
        tableWrapper: {
            width: "100%",
            overflowX: "auto",
            border: "1px solid #e2e8f0",
            borderRadius: "10px"
        },
        table: {
            width: "100%",
            minWidth: "540px",
            borderCollapse: "collapse",
            textAlign: "left"
        },
        th: {
            padding: "15px 18px",
            background: "#edf3fa",
            color: "#38516f",
            fontSize: "12px",
            textTransform: "uppercase",
            letterSpacing: "0.45px",
            fontWeight: "700",
            borderBottom: "1px solid #dce5f0",
            whiteSpace: "nowrap"
        },
        td: {
            padding: "16px 18px",
            color: "#334155",
            fontSize: "14px",
            lineHeight: "1.5",
            borderBottom: "1px solid #edf1f6"
        },
        badge: (eligible) => ({
            display: "inline-flex",
            alignItems: "center",
            padding: "6px 10px",
            borderRadius: "6px",
            fontSize: "12px",
            fontWeight: "600",
            whiteSpace: "nowrap",
            color: eligible ? "#21633b" : "#9b3434",
            background: eligible ? "#eaf5ed" : "#fbeeee",
            border: `1px solid ${
                eligible ? "#c8e5cf" : "#f0d0d0"
            }`
        }),
        aiHeader: {
            display: "flex",
            justifyContent: "space-between",
            alignItems: "flex-start",
            flexWrap: "wrap",
            gap: "16px",
            marginBottom: "10px"
        },
        aiIcon: {
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            width: "42px",
            height: "42px",
            flexShrink: 0,
            borderRadius: "10px",
            background: "#eaf1fc",
            color: "#285da8",
            fontSize: "17px",
            fontWeight: "700"
        },
        aiContent: {
            display: "flex",
            flexDirection: "column",
            gap: "12px",
            overflowWrap: "anywhere"
        },
        aiSubheading: {
            color: "#1e3a5f",
            fontSize: "15px",
            lineHeight: "1.5",
            fontWeight: "700",
            margin: "8px 0 0"
        },
        aiText: {
            display: "block",
            width: "100%",
            boxSizing: "border-box",
            textAlign: "left",
            whiteSpace: "pre-wrap",
            overflowWrap: "anywhere",
            lineHeight: "1.8",
            color: "#334155",
            fontSize: "14px",
            margin: 0
        },
        aiList: {
            margin: "0",
            paddingLeft: "22px",
            color: "#334155",
            fontSize: "14px",
            lineHeight: "1.8"
        },
        aiListItem: {
            paddingLeft: "4px",
            marginBottom: "6px"
        },
        primaryButton: {
            background: "#245da8",
            color: "#ffffff",
            border: "1px solid #245da8",
            padding: "11px 18px",
            borderRadius: "7px",
            cursor: aiLoading ? "wait" : "pointer",
            fontSize: "14px",
            fontWeight: "600",
            opacity: aiLoading ? 0.75 : 1
        },
        secondaryButton: {
            background: "#ffffff",
            color: "#285da8",
            border: "1px solid #cbd8e8",
            padding: "11px 18px",
            borderRadius: "7px",
            cursor: "pointer",
            fontSize: "14px",
            fontWeight: "600"
        },
        notice: {
            padding: "20px",
            borderRadius: "9px",
            background: "#f8fafc",
            border: "1px solid #e2e8f0",
            color: "#64748b",
            lineHeight: "1.7",
            fontSize: "14px"
        }
    };

    return (
        <div style={styles.page}>
            <Navbar />

            <main style={styles.container}>
                <header style={styles.pageHeader}>
                    <p style={styles.eyebrow}>Career Insights</p>
                    <h1 style={styles.title}>Skill Gap Analysis</h1>
                    <p style={styles.subtitle}>
                        Understand your current skill alignment and explore
                        suitable career opportunities.
                    </p>
                </header>

                {loading ? (
                    <section style={styles.panel}>
                        <p style={{ color: "#64748b", fontSize: "14px", margin: 0 }}>
                            Loading your skill analysis...
                        </p>
                    </section>
                ) : analysisError ? (
                    <section style={styles.panel}>
                        <h2 style={styles.sectionTitle}>
                            Unable to Load Analysis
                        </h2>
                        <p style={styles.sectionDescription}>
                            Please check your connection and try again.
                        </p>
                        <button
                            onClick={loadAnalysis}
                            style={styles.primaryButton}
                        >
                            Retry
                        </button>
                    </section>
                ) : analysis.length > 0 ? (
                    <>
                        <section
                            style={{
                                width: "100%",
                                maxWidth: "650px",
                                boxSizing: "border-box",
                                margin: "0 auto 28px",
                                padding: "22px 24px",
                                background:
                                    "linear-gradient(115deg, #ffffff 0%, #f1f6ff 100%)",
                                border: "1px solid #c9d9ee",
                                borderTop: "4px solid #285da8",
                                borderRadius: "12px",
                                boxShadow:
                                    "0 4px 14px rgba(31, 76, 135, 0.06)",
                                display: "flex",
                                flexDirection: "column",
                                alignItems: "center",
                                textAlign: "center"
                            }}
                        >
                            <p
                                style={{
                                    color: "#61748d",
                                    fontSize: "12px",
                                    fontWeight: "700",
                                    letterSpacing: "1.1px",
                                    textTransform: "uppercase",
                                    margin: "0 0 10px"
                                }}
                            >
                                Best Matched Role
                            </p>

                            <h2
                                style={{
                                    color: "#17365d",
                                    fontSize: "24px",
                                    fontWeight: "700",
                                    lineHeight: "1.4",
                                    margin: "0 0 18px",
                                    overflowWrap: "anywhere"
                                }}
                            >
                                {bestRole.roleName}
                            </h2>

                            <div
                                style={{
                                    display: "flex",
                                    flexDirection: "column",
                                    alignItems: "center",
                                    gap: "10px",
                                    borderTop: "1px solid #dce5f0",
                                    paddingTop: "15px",
                                    width: "100%"
                                }}
                            >
                                <span
                                    style={{
                                        color: "#64748b",
                                        fontSize: "13px",
                                        fontWeight: "600"
                                    }}
                                >
                                    Eligibility Status
                                </span>

                                <span
                                    style={{
                                        display: "inline-block",
                                        padding: "6px 13px",
                                        borderRadius: "6px",
                                        fontSize: "13px",
                                        fontWeight: "600",
                                        color: bestRole.eligible
                                            ? "#21633b"
                                            : "#9b3434",
                                        background: bestRole.eligible
                                            ? "#eaf5ed"
                                            : "#fbeeee",
                                        border: `1px solid ${
                                            bestRole.eligible
                                                ? "#c8e5cf"
                                                : "#f0d0d0"
                                        }`
                                    }}
                                >
                                    {bestRole.eligible
                                        ? "Eligible"
                                        : "Not Eligible"}
                                </span>
                            </div>
                        </section>

                        <section style={styles.panel}>
                            <h2 style={styles.sectionTitle}>
                                Role Match Overview
                            </h2>
                            <p style={styles.sectionDescription}>
                                Compare your skill match across the available
                                job roles.
                            </p>

                            <div style={styles.tableWrapper}>
                                <table style={styles.table}>
                                    <thead>
                                        <tr>
                                            <th style={styles.th}>Job Role</th>
                                            <th style={styles.th}>Skill Match</th>
                                            <th style={styles.th}>Eligibility</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        {analysis.map((role, index) => (
                                            <tr
                                                key={role.roleName || index}
                                                style={{
                                                    background:
                                                        index % 2 === 0
                                                            ? "#ffffff"
                                                            : "#f8fafc"
                                                }}
                                            >
                                                <td style={styles.td}>
                                                    <span
                                                        style={{
                                                            fontWeight:
                                                                index === 0
                                                                    ? "700"
                                                                    : "500",
                                                            color:
                                                                index === 0
                                                                    ? "#17365d"
                                                                    : "#334155"
                                                        }}
                                                    >
                                                        {role.roleName}
                                                    </span>
                                                </td>

                                                <td style={styles.td}>
                                                    {Number(
                                                        role.matchPercentage
                                                    ).toFixed(2)}%
                                                </td>

                                                <td style={styles.td}>
                                                    <span
                                                        style={styles.badge(
                                                            role.eligible
                                                        )}
                                                    >
                                                        {role.eligible
                                                            ? "Eligible"
                                                            : "Not Eligible"}
                                                    </span>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section style={styles.panel}>
                            <div style={styles.aiHeader}>
                                <div
                                    style={{
                                        display: "flex",
                                        gap: "14px",
                                        alignItems: "flex-start"
                                    }}
                                >
                                    <div style={styles.aiIcon}>AI</div>
                                    <div>
                                        <h2 style={styles.sectionTitle}>
                                            AI Career Recommendation
                                        </h2>
                                        <p
                                            style={{
                                                ...styles.sectionDescription,
                                                marginBottom: 0
                                            }}
                                        >
                                            Get personalized guidance to help
                                            plan your next learning steps.
                                        </p>
                                    </div>
                                </div>

                                <button
                                    onClick={loadAiRecommendation}
                                    disabled={aiLoading}
                                    style={styles.primaryButton}
                                >
                                    {aiLoading
                                        ? "Generating..."
                                        : showAiRecommendation
                                          ? "Generate Again"
                                          : "Get Recommendation"}
                                </button>
                            </div>

                            {showAiRecommendation && (
                                <div
                                    style={{
                                        marginTop: "22px",
                                        padding: "20px",
                                        border: "1px solid #e2e8f0",
                                        borderRadius: "9px",
                                        background: "#f8fafc",
                                        textAlign: "left",
                                        width: "100%",
                                        boxSizing: "border-box"
                                    }}
                                    aria-live="polite"
                                >
                                    {aiLoading ? (
                                        <p
                                            style={{
                                                color: "#64748b",
                                                fontSize: "14px",
                                                lineHeight: "1.7",
                                                margin: 0
                                            }}
                                        >
                                            Preparing your personalized
                                            recommendation. Please wait...
                                        </p>
                                    ) : (
                                        <p style={styles.aiText}>
                                            {aiRecommendation}
                                        </p>
                                    )}
                                </div>
                            )}
                        </section>

                        <div
                            style={{
                                display: "flex",
                                justifyContent: "flex-start",
                                flexWrap: "wrap",
                                gap: "12px"
                            }}
                        >
                            <button
                                onClick={downloadReport}
                                style={styles.primaryButton}
                            >
                                Download Report (PDF)
                            </button>

                            <button
                                onClick={() => navigate("/manage-skills")}
                                style={styles.secondaryButton}
                            >
                                Back to Manage Skills
                            </button>
                        </div>
                    </>
                ) : (
                    <section style={styles.panel}>
                        <h2 style={styles.sectionTitle}>
                            No Role Matches Available
                        </h2>
                        <p style={styles.sectionDescription}>
                            Add your skills and proficiency levels to view
                            your role match analysis.
                        </p>
                        <button
                            onClick={() => navigate("/manage-skills")}
                            style={styles.primaryButton}
                        >
                            Manage Skills
                        </button>
                    </section>
                )}
            </main>
        </div>
    );
}

export default Analysis;