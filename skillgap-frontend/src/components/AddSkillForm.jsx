
import { useState } from "react";
import SkillDropdown from "./SkillDropdown";
import { addStudentSkill } from "../services/studentSkillService";
import "./AddSkillForm.css";

function AddSkillForm({ onSkillAdded }) {
    const [selectedSkill, setSelectedSkill] = useState("");
    const [currentLevel, setCurrentLevel] = useState("");
    const [message, setMessage] = useState("");
    const [messageType, setMessageType] = useState("");
    const [saving, setSaving] = useState(false);

    const studentId = localStorage.getItem("studentId");

    const showMessage = (text, type) => {
        setMessage(text);
        setMessageType(type);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setMessageType("");

        if (!selectedSkill) {
            showMessage("Please select a skill.", "error");
            return;
        }

        if (currentLevel.trim() === "") {
            showMessage("Please enter your current level.", "error");
            return;
        }

        const level = Number(currentLevel);

        if (!Number.isFinite(level) || level < 0 || level > 100) {
            showMessage(
                "Current level must be between 0 and 100.",
                "error"
            );
            return;
        }

        if (!studentId || !Number.isFinite(Number(studentId))) {
            showMessage(
                "Student information is missing. Please log in again.",
                "error"
            );
            return;
        }

        const request = {
            student: {
                id: Number(studentId)
            },
            skill: {
                id: Number(selectedSkill)
            },
            currentLevel: level
        };

        try {
            setSaving(true);

            await addStudentSkill(request);

            showMessage("Skill added successfully!", "success");
            setSelectedSkill("");
            setCurrentLevel("");

            if (onSkillAdded) {
                onSkillAdded();
            }
        } catch (error) {
            console.error("Failed to add skill:", error);

            const status = error?.response?.status;
            const serverMessage =
                error?.response?.data?.message ??
                error?.response?.data?.error ??
                "";

            const errorText = String(serverMessage).toLowerCase();

            if (
                status === 409 ||
                errorText.includes("already exists") ||
                errorText.includes("already added") ||
                errorText.includes("duplicate")
            ) {
                showMessage(
                    "This skill already exists in your skills list.",
                    "error"
                );
            } else {
                showMessage(
                    "Failed to add skill. Please try again.",
                    "error"
                );
            }
        } finally {
            setSaving(false);
        }
    };

    return (
        <section className="add-skill-card">
            <div className="add-skill-heading">
                <div className="add-skill-icon" aria-hidden="true">
                    +
                </div>

                <div>
                    <h2>Add Skill</h2>
                    <p>
                        Select a skill and enter your current proficiency.
                    </p>
                </div>
            </div>

            <form className="add-skill-form" onSubmit={handleSubmit}>
                <div className="add-skill-field">
                    <label htmlFor="skill-search">Skill</label>

                    <SkillDropdown
                        selectedSkill={selectedSkill}
                        setSelectedSkill={setSelectedSkill}
                    />
                </div>

                <div className="add-skill-field">
                    <label htmlFor="current-level">
                        Current Level
                    </label>

                    <div className="level-input-wrapper">
                        <input
                            id="current-level"
                            type="number"
                            min="0"
                            max="100"
                            step="1"
                            value={currentLevel}
                            onChange={(e) =>
                                setCurrentLevel(e.target.value)
                            }
                            placeholder="Enter level"
                            disabled={saving}
                        />

                        <span className="level-suffix">/ 100</span>
                    </div>

                    <p className="add-skill-hint">
                        Enter a value between 0 and 100.
                    </p>
                </div>

                {message && (
                    <div
                        className={`add-skill-message ${messageType}`}
                        role="status"
                        aria-live="polite"
                    >
                        {message}
                    </div>
                )}

                <button
                    type="submit"
                    className="add-skill-submit"
                    disabled={saving}
                >
                    {saving ? "Saving..." : "Save Skill"}
                </button>
            </form>
        </section>
    );
}

export default AddSkillForm;
