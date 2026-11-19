import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import RegisterStudent from "./pages/RegisterStudent";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import AddStudentSkill from "./pages/AddStudentSkill";
import ManageSkills from "./pages/ManageSkills";
import Analysis from "./pages/Analysis";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {

    return (

        <BrowserRouter>

            <Routes>

                <Route path="/" element={<Home />} />

                <Route
                    path="/register"
                    element={<RegisterStudent />}
                />

                <Route
                    path="/login"
                    element={<Login />}
                />

                <Route
                    path="/dashboard"
                    element={<Dashboard />}
                />

                <Route
                    path="/add-skill"
                    element={<AddStudentSkill />}
                />
                <Route
                    path="/manage-skills"
                    element={
                        <ProtectedRoute>
                            <ManageSkills />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/analysis"
                    element={
                        <ProtectedRoute>
                            <Analysis />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/dashboard"
                    element={
                        <ProtectedRoute>
                            <Dashboard />
                        </ProtectedRoute>
                    }
                />

            </Routes>

        </BrowserRouter>

    );

}

export default App;