import api from "./api";

export const analyzeSkills = async (studentId) => {

    const response = await api.get(
        `/api/skillGap/student/${studentId}/all-roles`
    );

    return response.data;
};

export const getAiRecommendation = async (studentId) => {

    const response = await api.get(
        `/api/skillGap/student/${studentId}/ai-recommendation`
    );

    return response.data;
};