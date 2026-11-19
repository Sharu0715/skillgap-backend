import api from "./api";

export const addStudentSkill = async (studentSkill) => {
    const response = await api.post("/api/student-skills", studentSkill);
    return response.data;
};

export const getStudentSkills = async (studentId) => {
    const response = await api.get(`/api/student-skills/student/${studentId}`);
    return response.data;
};

export const updateStudentSkill = async (studentId, skillId, studentSkill) => {
    const response = await api.put(
        `/api/student-skills/student/${studentId}/skill/${skillId}`,
        studentSkill
    );
    return response.data;
};

export const deleteStudentSkill = async (studentId, skillId) => {
    await api.delete(
        `/api/student-skills/student/${studentId}/skill/${skillId}`
    );
};