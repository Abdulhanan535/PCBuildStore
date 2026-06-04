PC BUILD STORE - OOP Project Final Submission
=============================================

Group Members:
- Abdulhanan (rehanazizabdulhanan@gmail.com) - Dashboard, Reports, AI Chat
- Ahmad Mushtaq (2501803@students.au.edu.pk) - Billing Module
- Aitzaz (2501805@students.au.edu.pk) - Build Upgrades Module
- Haroon (haroonikram2512@gmail.com) - Build Configurator

Project Overview:
A PC part picker and configurator built with Java 17, Swing GUI, and MariaDB.
Users can browse 78+ components across 6 categories (CPU, GPU, RAM, Storage,
PSU, Motherboard), build custom PCs with compatibility filtering, upgrade
existing builds, purchase builds with receipts, and get AI-powered build
suggestions via an integrated chat bot.

Tech Stack:
- Java 17 + Swing GUI
- MariaDB 10.4 (XAMPP)
- JDBC (mysql-connector-j-9.7.0)
- NetBeans Ant build system
- OpenAI-compatible API (KoboldCPP) for AI chat

Submission Structure:
  Project/
  +-- Source_Code/        -> Java source files (src/ directory)
  +-- Database/           -> SQL schema and seed data
  +-- Report/             -> Full project report
  +-- Output_Screenshots/ -> Application screenshots
  +-- Analysis/           -> ER diagram and architecture docs
  +-- Presentation/       -> Presentation materials
  +-- Contribution_Details/ -> Individual contribution records
  +-- README.txt          -> This file

Setup Instructions:
1. Install XAMPP and start MySQL
2. Import database: mysql -u root < Database/pc_build_store.sql
3. Build: ant compile (or use NetBeans)
4. Run: ant run (or run Main.java)

GitHub Repository: https://github.com/Abdulhanan535/PCBuildStore
