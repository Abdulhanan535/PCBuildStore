# PC Build Store Management System

Java 17 + Swing + MySQL (MariaDB via XAMPP) + JDBC. NetBeans Ant project — no Maven/Gradle, no module-info.

## Structure

```
OOP Project/              ← git root
├── PCBuildStore/         ← NetBeans project
│   ├── src/com/project/  ← Java source
│   │   ├── Main.java     ← entrypoint (→ DashboardGUI)
│   │   ├── database/     ← DBConnection.java
│   │   ├── models/       ← Build.java, GPUOption.java, Bill.java
│   │   ├── dao/          ← CRUD DAOs
│   │   └── ui/           ← Swing GUIs
│   ├── database/         ← pc_build_store.sql (DB dump)
│   └── lib/              ← mysql-connector-j-9.7.0.jar
├── *.pdf                 ← project docs
└── mysql-connector-j-9.7.0.jar  ← standalone copy
```

## Commands

```bash
# Build & run (from PCBuildStore/)
ant clean; if ($?) { ant build }

# DB dump / restore
mysqldump -u root pc_build_store > PCBuildStore/database/pc_build_store.sql
mysql -u root pc_build_store < PCBuildStore/database/pc_build_store.sql
```

## Stack quirks

- **JDBC URL**: `jdbc:mysql://localhost:3306/pc_build_store?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`
- **Connector**: on Classpath, not Modulepath — set via `project.properties` `javac.classpath`
- **MySQL**: XAMPP default root/no password, MariaDB 10.4
- **Java**: source & target 17
- **main.class**: NetBeans uses `manifest.mf` — `project.properties` has `main.class=` empty by design

## Module allocation

| Member | Module | Branch |
|--------|--------|--------|
| Abdul Hanan (lead) | Dashboard & Reports + base | main |
| Haroon | Build Catalog | haroon-build-catalog |
| Aitzaz | GPU Upgrades | aitzaz-gpu-upgrades |
| Ahmad | Billing | ahmad-billing |

Vertical slices — each owns GUI + backend + DB for their module.

## DB

3 tables (`builds`, `gpu_options`, `bills`) with seed data from old PF project.

## Rules

- No code comments unless asked
- No emojis in files
- Keep output concise
