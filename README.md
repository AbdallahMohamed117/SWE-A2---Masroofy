# Masroofy - Student Budget Tracker

Masroofy is a desktop budget management application designed for students. It allows users to set a monthly allowance, track daily spending by category, view transaction history, and stay within a calculated daily safe limit. The app is protected by a PIN code for privacy.

---

## Project Info

| Field | Details |
|---|---|
| **Project Name** | Masroofy |
| **Course** | SWE Assignment 2 (SWE-A2) |
| **Language** | Java 9+ |
| **UI Framework** | JavaFX 21 |
| **Database** | SQLite (`masroofy.db`) |

---

## Features

- Splash screen on launch
- PIN-based access (set on first run, required on subsequent runs)
- Budget setup: enter allowance, start date, and end date
- Automatic daily safe limit calculation (recalculated daily)
- Dashboard: shows remaining balance and daily limit at a glance
- Quick Entry: add spending transactions by category and amount
- Transaction History: view, filter, and edit past transactions
- Settings: change PIN or reset the app to start over
- Persistent local storage using SQLite

---

## Database Schema

| Table | Description |
|---|---|
| `Category` | Spending categories (e.g., Food, Transport) |
| `Budget` | Allowance, dates, daily limits |
| `Student` | PIN code, active state, linked budget |
| `Transactions` | Individual spending records with timestamp and category |

---

## Requirements

- Java 9 or higher
- Maven 3.6+
- JavaFX 21
- No internet connection required (fully offline)

---

## How to Run

1. Open the project using IntelliJ
2. Make sure required dependencies are installed
3. Run the main file `Launcher.java`
4. On **first launch**, you will be prompted to set up your budget and create a PIN.  
   On **subsequent launches**, enter your PIN to access the dashboard.

---

## Project Structure

```
src/main/java/com/example/masroofy/
├── App/                    # Application entry point, screen routing, splash screen
├── Controller/             # Screen controllers (Dashboard, History, QuickEntry, etc.)
├── Model/                  # Business logic and data models
│   └── Entity/             # Data entities: Budget, Category, Transaction
├── View/                   # JavaFX UI views for each screen
├── Listener/               # Event listener interfaces between View and Controller
├── Database/               # SQLite database connection handler
├── util/                   # Utility classes (DateUtil, QuickEntryValidator)
├── Launcher.java           # Application launcher
└── BudgetApplication.java  # JavaFX Application class

masroofy.db                 # SQLite database file (created automatically on first run)
masroofy.sql                # SQL schema file for reference
pom.xml                     # Maven build configuration
```

---

## Screens

| # | Screen | Description |
|---|---|---|
| 1 | Splash | Loading/intro screen |
| 2 | Setup | First-time budget configuration |
| 3 | PIN | PIN entry screen |
| 4 | Dashboard | Main screen with balance and daily limit summary |
| 5 | QuickEntry | Add or edit a spending transaction |
| 6 | History | View and manage past transactions |
| 7 | Settings | Change PIN or reset the app |
