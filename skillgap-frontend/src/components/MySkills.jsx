import { useEffect, useState } from "react";
import {
    getStudentSkills,
    deleteStudentSkill,
    updateStudentSkill
} from "../services/studentSkillService";

function MySkills({ refresh }) {

    const [skills, setSkills] = useState([]);
    const [editingSkillId, setEditingSkillId] = useState(null);
    const [updatedLevel, setUpdatedLevel] = useState("");

    const studentId = localStorage.getItem("studentId");

    useEffect(() => {

        loadSkills();

    }, [refresh]);

    const loadSkills = async () => {

        try {

            const data = await getStudentSkills(studentId);

            setSkills(data);

        } catch (error) {

            console.log(error);

            setSkills([]);

        }

    };

    const handleDelete = async (skillId) => {

        try {

            await deleteStudentSkill(studentId, skillId);

            alert("Skill deleted successfully");

            loadSkills();

        } catch (error) {

            console.log(error);

            alert("Delete Failed");

        }

    };

    const saveSkill = async (skillId) => {

        if (updatedLevel < 0 || updatedLevel > 100) {

            alert("Current Level must be between 0 and 100");

            return;

        }

        try {

            await updateStudentSkill(

                studentId,

                skillId,

                {
                    currentLevel: Number(updatedLevel)
                }

            );

            alert("Skill Updated Successfully");

            setEditingSkillId(null);

            loadSkills();

        } catch (error) {

            console.log(error);

            alert("Update Failed");

        }

    };

    return (

        <div
            style={{
                marginTop: "30px",
                display: "flex",
                justifyContent: "center"
            }}
        >

            {skills.length === 0 ? (

                <h3>No Skills Added</h3>

            ) : (

                <table
                    style={{
                        width: "650px",
                        borderCollapse: "collapse",
                        textAlign: "center",
                        boxShadow: "0 2px 8px rgba(0,0,0,0.15)"
                    }}
                >

                    <thead>

                        <tr
                            style={{
                                backgroundColor: "#1976d2",
                                color: "white"
                            }}
                        >

                            <th
                                style={{
                                    padding: "12px"
                                }}
                            >
                                Skill
                            </th>

                            <th
                                style={{
                                    padding: "12px"
                                }}
                            >
                                Current Level
                            </th>

                            <th
                                style={{
                                    padding: "12px"
                                }}
                            >
                                Actions
                            </th>

                        </tr>

                    </thead>

                    <tbody>

                        {skills.map((skill) => (

                            <tr key={skill.skillId}>

                                <td
                                    style={{
                                        padding: "12px",
                                        border: "1px solid #ddd"
                                    }}
                                >
                                    {skill.skillName}
                                </td>

                                <td
                                    style={{
                                        padding: "12px",
                                        border: "1px solid #ddd"
                                    }}
                                >

                                    {editingSkillId === skill.skillId ? (

                                        <input
                                            type="number"
                                            min="0"
                                            max="100"
                                            value={updatedLevel}
                                            onChange={(e) =>
                                                setUpdatedLevel(e.target.value)
                                            }
                                            style={{
                                                width: "60px",
                                                textAlign: "center"
                                            }}
                                        />

                                    ) : (

                                        skill.currentLevel

                                    )}

                                </td>

                                <td
                                    style={{
                                        padding: "12px",
                                        border: "1px solid #ddd"
                                    }}
                                >

                                    {editingSkillId === skill.skillId ? (

                                        <>

                                            <button
                                                onClick={() =>
                                                    saveSkill(skill.skillId)
                                                }
                                                style={{
                                                    marginRight: "8px",
                                                    padding: "6px 12px"
                                                }}
                                            >
                                                Save
                                            </button>

                                            <button
                                                onClick={() =>
                                                    setEditingSkillId(null)
                                                }
                                                style={{
                                                    padding: "6px 12px"
                                                }}
                                            >
                                                Cancel
                                            </button>

                                        </>

                                    ) : (

                                        <>

                                            <button
                                                onClick={() => {

                                                    setEditingSkillId(skill.skillId);

                                                    setUpdatedLevel(skill.currentLevel);

                                                }}
                                                style={{
                                                    marginRight: "8px",
                                                    padding: "6px 12px"
                                                }}
                                            >
                                                Edit
                                            </button>

                                            <button
                                                onClick={() =>
                                                    handleDelete(skill.skillId)
                                                }
                                                style={{
                                                    padding: "6px 12px",
                                                    background: "#d32f2f",
                                                    color: "white",
                                                    border: "none",
                                                    borderRadius: "4px",
                                                    cursor: "pointer"
                                                }}
                                            >
                                                Delete
                                            </button>

                                        </>

                                    )}

                                </td>

                            </tr>

                        ))}

                    </tbody>

                </table>

            )}

        </div>

    );

}

export default MySkills;