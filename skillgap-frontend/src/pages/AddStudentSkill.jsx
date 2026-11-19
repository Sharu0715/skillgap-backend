import { useState } from "react";
import api from "../services/api";


function AddStudentSkill() {


    const [skillId, setSkillId] = useState("");

    const [currentLevel, setCurrentLevel] = useState("");


    const studentId = localStorage.getItem("studentId");



    const addSkill = async (e) => {

        e.preventDefault();


        try {


            const response = await api.post(
                "/api/student-skills",
                {
                    student: {
                        id: studentId
                    },

                    skill: {
                        id: skillId
                    },

                    currentLevel: currentLevel
                }
            );


            console.log(response.data);


            alert("Skill Added Successfully");


        }

        catch(error){

            console.log(error);

            alert("Failed to add skill");

        }

    };



    return (

        <div>


            <h1>
                Add Student Skill
            </h1>


            <form onSubmit={addSkill}>


                <div>

                    <label>
                        Skill ID:
                    </label>


                    <input

                        type="number"

                        value={skillId}

                        onChange={
                            (e)=>setSkillId(e.target.value)
                        }

                    />

                </div>



                <div>

                    <label>
                        Current Level:
                    </label>


                    <input

                        type="number"

                        value={currentLevel}

                        onChange={
                            (e)=>setCurrentLevel(e.target.value)
                        }

                    />

                </div>



                <button type="submit">

                    Add Skill

                </button>


            </form>


        </div>

    );

}


export default AddStudentSkill;