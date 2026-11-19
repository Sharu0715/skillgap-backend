import api from "./api";

export const getAllSkills = async () => {

    const response = await api.get("/api/skills/dropdown");

    return response.data;

};