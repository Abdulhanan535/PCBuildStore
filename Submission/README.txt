README
PC Build Store Management System

================================================================================
PROJECT
================================================================================

A Java Swing app for managing PC builds, GPU upgrades, and billing.
Built as an OOP course project using modular architecture.

================================================================================
TEAM
================================================================================

Abdul Hanan (Lead) - Dashboard & Reports
Haroon Ikram       - Build Catalog
Aitzaz             - GPU Upgrades
Ahmad              - Billing

GitHub: https://github.com/Abdulhanan535/PCBuildStore

================================================================================
FOLDER STRUCTURE
================================================================================

Submission/
  Source_Code/src/com/project/   -- all Java files
  Source_Code/lib/               -- MySQL connector JAR
  Source_Code/build.xml          -- Ant build file
  Source_Code/nbproject/         -- NetBeans config
  Database/                      -- SQL dump
  Report/                        -- project report + ER diagram
  Output_Screenshots/            -- screenshots go here
  Contribution_Details/          -- who did what
  Analysis/                      -- system analysis
  Presentation/                  -- slides
  README.txt                     -- this file

================================================================================
SETUP
================================================================================

Prerequisites:
  1. Java 17+
  2. XAMPP with MySQL running
  3. NetBeans (or any Java IDE)

Database setup:
  1. Start XAMPP MySQL
  2. Open terminal
  3. mysql -u root < Database/pc_build_store.sql

Run the app:
  Ant: ant run
  NetBeans: Right-click project -> Run

================================================================================
TECH
================================================================================

  Java 17
  Swing (GUI)
  MariaDB 10.4 (XAMPP)
  MySQL Connector/J 9.7.0
  Apache Ant
  NetBeans
  Git

================================================================================
DATABASE
================================================================================

3 tables:
  builds (184 rows) - PC build configurations
  gpu_options (59 rows) - GPU upgrade options
  bills (grows) - purchase records

Price range: 50k-500k PKR
Score range: 0-120

================================================================================
FEATURES
================================================================================

Dashboard:
  - Shows total builds, revenue, avg score, bills count
  - Click cards to go to each module
  - Stats refresh automatically

Build Catalog:
  - Table with all builds
  - Search by budget
  - Add/edit/delete builds
  - Color coding for CPU/GPU types

GPU Upgrades:
  - Table with GPU options
  - Filter by brand (Nvidia/AMD)
  - Add new GPU options

Billing:
  - Select budget and buy builds
  - GPU upgrade option during purchase
  - View bill history
  - See receipts
  - Delete bills

================================================================================
USAGE
================================================================================

1. Open the app
2. Dashboard shows stats
3. Click "Build Catalog" to browse builds
4. Click "GPU Upgrades" to see GPU options
5. Click "Billing" to buy a build:
   - Pick a budget from dropdown
   - Click "Purchase Build"
   - Select a build
   - Choose GPU upgrade (optional)
   - Confirm
6. Go back to Dashboard to see updated stats

================================================================================
