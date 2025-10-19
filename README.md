# 🧭 Task Tracker Backend

[![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Java](https://img.shields.io/badge/Java-21-blue.svg)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen.svg)]()
[![License](https://img.shields.io/badge/license-MIT-yellow.svg)]()
[![Database](https://img.shields.io/badge/database-H2%20%7C%20MySQL-lightgrey.svg)]()

A powerful **Spring Boot–based backend** for managing tasks efficiently.  
This project is designed for both personal productivity and team collaboration, offering a secure REST API for user management and task operations.

---

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## 🌟 Overview

**Task Tracker** is a Spring Boot web backend that provides RESTful APIs for managing user accounts and their associated tasks.  
It supports creating, updating, deleting, and tracking the progress of tasks while maintaining secure user authentication.

---

## 🚀 Features

✅ User registration & login (JWT authentication)  
✅ Create, update, delete, and view tasks  
✅ Assign tasks to users  
✅ Track task completion status  
✅ Exception handling & validation  
✅ Secure API endpoints  
✅ H2/MySQL database configuration  
✅ Clean modular architecture (Controller → Service → Repository)

---

## 🧩 Tech Stack

| Layer | Technology |
|-------|-------------|
| **Backend Framework** | Spring Boot 3.3+ |
| **Language** | Java 21 |
| **Build Tool** | Maven |
| **Database** | H2 (dev) / MySQL (prod) |
| **Security** | Spring Security + JWT |
| **ORM** | Spring Data JPA |
| **Validation** | Hibernate Validator |
| **Testing** | JUnit / Mockito |

---

## 📂 Project Structure

```
Task-Tracker/
├── src/
│   ├── main/
│   │   ├── java/com/tasktracker/
│   │   │   ├── controller/        # REST controllers
│   │   │   ├── service/           # Business logic
│   │   │   ├── repository/        # JPA repositories
│   │   │   ├── model/             # Entities (User, Task, etc.)
│   │   │   └── config/            # Security and CORS configuration
│   │   └── resources/
│   │       ├── application.yml    # Configurations
│   │       └── data.sql           # Sample data (optional)
│   └── test/                      # Unit & integration tests
├── pom.xml
└── README.md
```

---

## ⚙️ Getting Started

### Prerequisites

Make sure you have:
- Java 21 or above
- Maven 3.9+
- (Optional) MySQL database setup

### Installation

```bash
git clone https://github.com/Ajay9760/Task-Tracker.git
cd Task-Tracker
mvn clean install
```

---

## 🛠 Configuration

Edit your environment variables in `application.yml` or `application.properties`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tasktracker
    username: root
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  security:
    jwt:
      secret: your_secret_key
```

You can switch to **H2** for local testing:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
```

---

## ▶️ Running the Application

To start the server:
```bash
mvn spring-boot:run
```

Once started, the backend will be available at:
```
http://localhost:8080
```

---

## 🔗 API Endpoints

| Method | Endpoint | Description | Auth |
|--------|-----------|--------------|------|
| **POST** | `/api/auth/register` | Register a new user | ❌ |
| **POST** | `/api/auth/login` | Authenticate & get JWT | ❌ |
| **GET** | `/api/tasks` | Get all tasks for logged-in user | ✅ |
| **POST** | `/api/tasks` | Create a new task | ✅ |
| **PUT** | `/api/tasks/{id}` | Update a task | ✅ |
| **DELETE** | `/api/tasks/{id}` | Delete a task | ✅ |

---

## 🖼 Screenshots

_Add screenshots of Swagger UI, Postman results, or database structure here._

Example:
```
/assets/screenshots/dashboard.png
/assets/screenshots/api-response.png
```

---

## 🤝 Contributing

1. Fork this repository  
2. Create a feature branch:  
   ```bash
   git checkout -b feature/YourFeature
   ```  
3. Commit your changes:  
   ```bash
   git commit -m "Add: YourFeature"
   ```  
4. Push to your branch and open a Pull Request  

---

## 📜 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 📧 Contact

**Author:** Ajay Adire  
**GitHub:** [Ajay9760](https://github.com/Ajay9760)  
**Project Link:** [Task Tracker](https://github.com/Ajay9760/Task-Tracker)

---

> _“Track your tasks. Stay organized. Deliver efficiently.”_
