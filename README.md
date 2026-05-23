# PC Build Store Management System

Java 17 + Swing + MySQL (MariaDB via XAMPP) + JDBC. NetBeans Ant project — no Maven/Gradle, no module-info.

## Structure

```
PCBuildStore/                 ← git root
├── src/com/project/
│   ├── Main.java             ← entrypoint (→ DashboardGUI)
│   ├── database/             ← DBConnection.java
│   ├── models/               ← Build.java, GPUOption.java, Bill.java
│   ├── dao/                  ← CRUD DAOs
│   └── ui/                   ← Swing GUIs
├── database/                 ← pc_build_store.sql (DB dump)
├── lib/                      ← mysql-connector-j-9.7.0.jar
├── nbproject/                ← NetBeans project config
├── build.xml                 ← Ant build file
├── manifest.mf               ← JAR manifest
└── test/                     ← test sources
```

## Commands

```bash
# Build
ant clean; if ($?) { ant build }

# DB dump / restore
mysqldump -u root pc_build_store > database/pc_build_store.sql
mysql -u root pc_build_store < database/pc_build_store.sql
```

## Stack

- **JDBC URL**: `jdbc:mysql://localhost:3306/pc_build_store?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`
- **Connector**: `lib/mysql-connector-j-9.7.0.jar` on Classpath, not Modulepath
- **MySQL**: XAMPP default — root, no password, MariaDB 10.4
- **Java**: source & target 17
- **main.class**: NetBeans uses `manifest.mf`, so `project.properties` leaves it empty

## Module allocation

| Member | Module | Branch |
|--------|--------|--------|
| Abdul Hanan (lead) | Dashboard & Reports + base | main |
| Haroon | Build Catalog | haroon-build-catalog |
| Aitzaz | GPU Upgrades | aitzaz-gpu-upgrades |
| Ahmad | Billing | ahmad-billing |

Vertical slices — each owns GUI + backend + DB for their module.

## DB

3 tables (`builds`, `gpu_options`, `bills`) with seed data.
