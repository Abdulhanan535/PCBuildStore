# PC Build Store

A PC part picker and configurator built with Java 17, Swing GUI, and MariaDB.

## Features
- Browse 78+ components across 6 categories (CPU, GPU, RAM, Storage, PSU, Motherboard)
- Build custom PCs with real-time compatibility filtering
- Upgrade existing builds with smart compatibility checks
- Purchase builds and view/print/save receipts
- AI-powered build suggestions via integrated chat bot
- Dark theme UI with real product images

## Tech Stack
- Java 17 + Swing GUI
- MariaDB 10.4 (XAMPP)
- JDBC (mysql-connector-j-9.7.0)
- NetBeans Ant build system
- OpenAI-compatible API (KoboldCPP) for AI chat

## Setup
1. Install XAMPP and start MySQL
2. Import database: `mysql -u root < database/pc_build_store.sql`
3. Build: `ant compile`
4. Run: `ant run`

## Team
- **Abdulhanan** - Dashboard, Reports, AI Chat Bot
- **Ahmad Mushtaq** - Billing Module
- **Aitzaz** - Build Upgrades Module
- **Haroon** - Build Configurator

## Database Schema
- `categories` - Part categories (CPU, GPU, etc.)
- `parts` - 78+ components with specs and pricing
- `builds` - Custom PC builds
- `build_parts` - Parts in each build
- `bills` - Purchase records with receipts
