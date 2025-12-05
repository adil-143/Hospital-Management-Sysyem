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
```

🔌 Step 2 — Setup JDBC Driver (MySQL Connector/J)

This project requires the official MySQL JDBC driver.

1. Download the driver

Download from the official MySQL website:

```👉 https://dev.mysql.com/downloads/connector/j/```

Select Platform Independent → Download ZIP

2. Extract and copy the .jar file

Inside the extracted ZIP, find:

mysql-connector-j-xx.x.x.jar


Create a lib/ folder in your project and place the JAR there:

HOSPITALMANAGEMENT<br>
 ├── src<br>
 ├── lib<br>
 │    └── mysql-connector-j-xx.x.x.jar<br>
 └── README.md<br>

3. Configure VS Code to use the JAR

Create or update:

```.vscode/settings.json```


with:
```
{
  "java.project.sourcePaths": ["src"],
  "java.project.outputPath": "bin",
  "java.project.referencedLibraries": [
    "lib/**/*.jar"
  ]
}
```

This ensures the JDBC driver loads correctly when running the project.

🔑 Step 3 — Configure Database Credentials

Open:

```src/DBHelper.java```


Edit your MySQL URL, username, and password:

```
private static final String URL = "jdbc:mysql://localhost:3306/hospital_db?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";   // your MySQL username
private static final String PASS = "your_password_here"; // your MySQL password
```


If your MySQL has no password, use:

```private static final String PASS = "";```


If your MySQL runs on a different port (not 3306):
```
jdbc:mysql://localhost:PORT_NUMBER/hospital_db
```

▶️ Running the Project (VS Code)
1. Open the project folder

Open the HOSPITALMANAGEMENT folder in VS Code.

2. Do NOT use Code Runner

Instead:

Click Run Java above the main() method
OR

Right-click the file → Run Java

This ensures the JDBC driver loads properly.

3. The GUI will appear

You will now see:

Patient fields

Add / View / Search / Discharge buttons

A table area

📘 Usage Guide
➕ Add Patient

Enter: ID, Name, Age, Disease, Phone

Click Add Patient

The patient will be inserted into the MySQL database

📋 View All Patients

Click View All

All records from the patients table will be displayed in the table

🔍 Search Patient by ID

Enter a patient ID

Click Search by ID

The GUI will show only the matching patient record

🚫 Discharge Patient

Enter the patient ID

Click Discharge by ID

The patient’s admitted value becomes 0 (discharged)

🧹 Optional: Recommended .gitignore

Add the following to avoid uploading unnecessary files:
```
bin/
lib/
*.class
*.jar
.vscode/
.DS_Store
Thumbs.db
```

📦 SQL Import File (Recommended for Users)

Create a folder:
```
sql/
```

Inside it create hospital_db.sql:
```
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
```

Anyone cloning your project can import this file in MySQL Workbench:

Open MySQL Workbench

Go to: Server → Data Import

Select hospital_db.sql

Click Start Import

🎉 You’re Done!

Your Hospital Management System is now fully configured with:

✔ Java Swing GUI
✔ MySQL Database
✔ JDBC connectivity
✔ CRUD operations
✔ Complete setup instructions for all users
