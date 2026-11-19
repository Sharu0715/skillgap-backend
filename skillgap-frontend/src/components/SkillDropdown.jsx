
import { useEffect, useRef, useState } from "react";
import { getAllSkills } from "../services/skillService";

function SkillDropdown({ selectedSkill, setSelectedSkill }) {
    const [skills, setSkills] = useState([]);
    const [search, setSearch] = useState("");
    const [isOpen, setIsOpen] = useState(false);

    const dropdownRef = useRef(null);

    useEffect(() => {
        loadSkills();
    }, []);

    // Close dropdown when clicking outside
    useEffect(() => {
        const handleOutsideClick = (event) => {
            if (
                dropdownRef.current &&
                !dropdownRef.current.contains(event.target)
            ) {
                setIsOpen(false);
            }
        };

        document.addEventListener("mousedown", handleOutsideClick);

        return () => {
            document.removeEventListener(
                "mousedown",
                handleOutsideClick
            );
        };
    }, []);

    const loadSkills = async () => {
        try {
            const data = await getAllSkills();

            const skillsList = Array.isArray(data)
                ? data
                : data?.content ?? [];

            setSkills(skillsList);
        } catch (error) {
            console.error("Failed to load skills:", error);
            alert("Failed to load skills");
        }
    };

    const filteredSkills = skills.filter((skill) =>
        skill.skillName
            .toLowerCase()
            .includes(search.trim().toLowerCase())
    );

    const selectedSkillName =
        skills.find(
            (skill) => String(skill.id) === String(selectedSkill)
        )?.skillName ?? "";

    const handleSelect = (skill) => {
        setSelectedSkill(String(skill.id));
        setSearch(skill.skillName);
        setIsOpen(false);
    };

    return (
        <div
            ref={dropdownRef}
            className="skill-dropdown"
            style={{ position: "relative" }}
        >
            <input
                type="text"
                value={isOpen ? search : selectedSkillName}
                placeholder="Search skill (e.g. Java)"
                onFocus={() => {
                    setSearch("");
                    setIsOpen(true);
                }}
                onChange={(e) => {
                    setSearch(e.target.value);
                    setIsOpen(true);
                    setSelectedSkill("");
                }}
                onKeyDown={(e) => {
                    if (e.key === "Escape") {
                        setIsOpen(false);
                    }
                }}
                autoComplete="off"
            />

            {isOpen && (
                <ul
                    style={{
                        position: "absolute",
                        top: "100%",
                        left: 0,
                        right: 0,
                        width: "100%",
                        maxHeight: "150px",
                        overflowY: "auto",
                        background: "white",
                        border: "1px solid #ccc",
                        borderRadius: "4px",
                        padding: 0,
                        margin: 0,
                        listStyle: "none",
                        zIndex: 1000,
                        boxSizing: "border-box"
                    }}
                >
                    {filteredSkills.length > 0 ? (
                        filteredSkills.map((skill) => (
                            <li
                                key={skill.id}
                                onMouseDown={() => handleSelect(skill)}
                                style={{
                                    padding: "10px",
                                    cursor: "pointer",
                                    color: "#222"
                                }}
                            >
                                {skill.skillName}
                            </li>
                        ))
                    ) : (
                        <li
                            style={{
                                padding: "10px",
                                color: "#666"
                            }}
                        >
                            No matching skills found
                        </li>
                    )}
                </ul>
            )}
        </div>
    );
}

export default SkillDropdown;
