# SkillGap Backend

SkillGap is a Spring Boot backend application that analyzes the gap between a student's skills and the skills required for a specific job role using weight-based evaluation.

---

## 🚧 Project Status
In Development

---

## 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Jakarta Validation
- Hibernate
- PostgreSQL (v15)
- Lombok
- Maven
- Postman (API Testing)
- IntelliJ IDEA (Development)

---

## 🏗 Architecture

This project follows layered architecture:

Entity → Repository → Service → Controller

- Repository layer handles database operations
- Service layer contains business logic
- Controller layer exposes REST APIs

---

## 📌 Core Domain Entities

### 1. Student
Stores student details.

### 2. Skill
Master table for all available skills.

### 3. StudentSkill
Maps students to skills with proficiency level.

### 4. JobRole
Represents job roles (e.g., Backend Developer).

### 5. RoleSkillWeight
Defines required skills and weightage for each job role.

---

## 🧠 Current Business Logic

- Compares student skills with job role required skills
- Uses weightage to calculate skill gap
- Returns structured response using DTOs
- Input validation applied where required
- APIs tested using Postman

---

## 🗄 Database Configuration

PostgreSQL 15 is used as the database.

Hibernate dialect configured:

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

---

## ▶️ How to Run

1. Clone the repository
2. Configure PostgreSQL in application.properties
3. Run the Spring Boot application
4. Test APIs using Postman

---

## 🔜 Planned Improvements

- Improve skill gap calculation accuracy
- JWT-based authentication
- Global exception handling
- Role-based authorization
