Task-Tracker

A simple task-tracking application built in Java.

Table of Contents

Overview

Features

Tech Stack

Getting Started

Prerequisites

Installation

Running the Application

Usage

Project Structure

Contributing

License

Contact

Overview

Task-Tracker is a lightweight Java application designed to help you manage tasks — add, edit, mark complete, and remove tasks quickly and efficiently. It is ideal for individuals or small teams looking for a minimalistic solution without heavy dependencies.

Features

Create new tasks with title and description

Edit existing tasks

Mark tasks as completed / uncompleted

Delete tasks

Persistent storage (if implemented, insert details)

Simple, clean user interface (console / GUI / web)

Tech Stack

Language: Java (100%) 
GitHub

Build Tool: Maven (see pom.xml)

Additional Libraries / Frameworks: (list if any e.g., Spring Boot, JavaFX, etc.)

Persistence: (e.g., file-based, H2 database, SQLite – update as appropriate)

Testing: (JUnit, etc. — update accordingly)

Getting Started
Prerequisites

Java JDK 17 (or whichever version your project uses)

Maven

(Any other tools if needed)

Installation

Clone the repository:

git clone https://github.com/Ajay9760/Task-Tracker.git  
cd Task-Tracker  


Build the project:

mvn clean install  

Running the Application

Depending on how the application is set up, you might run it as:

mvn exec:java -Dexec.mainClass="com.example.Main"  


or, if it produces a JAR file:

java -jar target/task-tracker-1.0.jar  


Update this with the actual main class and JAR name.

Usage

Launch the application.

Add a task by selecting “New Task” (or via console prompt).

Provide a title and description.

Review the list of tasks; mark tasks complete when done or delete them.

(If supported) Tasks persist across sessions so you can continue where you left off.
Include screenshots or commands here if needed.

Project Structure
Task-Tracker/
├── src/
│   ├── main/
│   │   ├── java/…  
│   │   └── resources/…  
│   └── test/…
├── .gitignore  
├── pom.xml  
└── README.md  

 

Contact

Created by Adire Ajay Kumar — feel free to reach out via GitHub.
Project Link: https://github.com/Ajay9760/Task-Tracker
