PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS Category (
    category_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name TEXT    NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Budget (
    budget_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    allowance         REAL    NOT NULL,
    start_date        TEXT    NOT NULL,
    end_date          TEXT    NOT NULL,
    daily_safe_limit  REAL    NOT NULL,
    original_daily_limit REAL,
    last_recalc_date  TEXT
);

CREATE TABLE IF NOT EXISTS Student (
    student_id      INTEGER PRIMARY KEY AUTOINCREMENT,
    student_pincode TEXT NOT NULL,
    student_state   TEXT    NOT NULL CHECK(student_state IN ('ACTIVE', 'INACTIVE')),
    budget_id       INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (budget_id) REFERENCES Budget(budget_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Transactions (
    transaction_id        INTEGER PRIMARY KEY AUTOINCREMENT,
    transaction_amount    REAL    NOT NULL,
    transaction_timestamp INTEGER NOT NULL,
    student_id            INTEGER NOT NULL,
    category_id           INTEGER NOT NULL,
    FOREIGN KEY (student_id)   REFERENCES Student(student_id)   ON DELETE CASCADE,
    FOREIGN KEY (category_id)  REFERENCES Category(category_id) ON DELETE RESTRICT
);