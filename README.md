# 🏥 Hospital Management System  
*A Java Swing Desktop Application with MySQL Integration*

This is a simple yet fully functional **Hospital Management System** built using **Java (Swing)** for the GUI and **MySQL** for data storage. It demonstrates how to build a desktop application with proper CRUD functionalities and database connectivity via JDBC.

---

## 🚀 Features

- Add new patients  
- View all patients  
- Search patients by ID  
- Discharge patients  
- Display records in interactive tables  
- Real MySQL backend using JDBC  
- Clean and simple Swing-based GUI  
- Fully open-source and beginner-friendly  

---

## 🛠️ Technologies Used

| Technology | Purpose |
|-----------|----------|
| **Java (JDK 17+)** | Backend logic + GUI |
| **Java Swing** | Desktop UI |
| **MySQL** | Persistent data storage |
| **JDBC (Connector/J)** | MySQL connectivity |
| **VS Code** | Development environment |

---

## 📁 Project Structure
HOSPITALMANAGEMENT<br>
│<br>
├── src<br>
│ ├── DBHelper.java<br>
│ └── HospitalManagementGUI.java<br>
│<br>
├── .gitignore<br>
└── README.md<br>

## 🗄️ Database Setup (MySQL)

This project uses **MySQL** as the backend database.  
Follow the steps below to set it up correctly.

---

### 📥 Step 1 — Create Database & Tables

You can use the SQL file provided in the `sql/` folder or run this manually:

```sql
CREATE DATABASE hospital_db;

USE hospital_db;

CREATE TABLE patients (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    disease VARCHAR(100),
    phone VARCHAR(20),
    admitted TINYINT(1)
);
