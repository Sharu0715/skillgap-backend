
import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => Promise.reject(error)
);

let isRedirectingToLogin = false;

api.interceptors.response.use(
    (response) => response,
    (error) => {
        const status = error.response?.status;
        const requestUrl = error.config?.url || "";

        const isAuthRequest = requestUrl.startsWith("/auth/");

        if (
            status === 401 &&
            !isAuthRequest &&
            localStorage.getItem("token") &&
            !isRedirectingToLogin
        ) {
            isRedirectingToLogin = true;

            localStorage.removeItem("token");
            localStorage.removeItem("studentId");

            alert("Your session has expired. Please log in again.");

            window.location.replace("/login");
        }

        return Promise.reject(error);
    }
);

export default api;
