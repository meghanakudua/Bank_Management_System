create database bank_project_1;

use bank_project_1;

CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(10) UNIQUE,
    card_number VARCHAR(20) UNIQUE,
    pin VARCHAR(10),
    branch VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE customers 
ADD COLUMN last_salary_credit DATE DEFAULT '2025-03-10';


describe customers;

select * from customers;

select * from transactions;

ALTER TABLE customers ADD balance DECIMAL(10,2) DEFAULT 500;


ALTER TABLE customers
ADD COLUMN name VARCHAR(100) NOT NULL FIRST,
ADD COLUMN email VARCHAR(100) NOT NULL AFTER name;

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20),
    type VARCHAR(20), -- 'deposit', 'withdrawal', 'transfer_in', 'transfer_out'
    amount DECIMAL(10, 2),
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    related_account VARCHAR(20), -- For transfers: the other account number (to or from)
    description TEXT
);

CREATE TABLE bank_staff (
    id INT PRIMARY KEY AUTO_INCREMENT,
    bank_user_number VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    branch VARCHAR(50) NOT NULL
);

select * from bank_staff;

CREATE TABLE atm_balance (
    branch VARCHAR(50) PRIMARY KEY,
    balance DECIMAL(12, 2) NOT NULL DEFAULT 50000.00
);

INSERT INTO atm_balance (branch, balance)
VALUES 
('Bangalore', 300000.00),
('Chennai', 300000.00),
('Mangalore', 300000.00),
('Mumbai', 300000.00)
ON DUPLICATE KEY UPDATE balance = balance;

-- Add employment status
ALTER TABLE customers
ADD COLUMN employment_status ENUM('employed', 'unemployed') NOT NULL DEFAULT 'unemployed' AFTER email;

-- Add account type
ALTER TABLE customers
ADD COLUMN account_type ENUM('salary', 'savings') NOT NULL DEFAULT 'savings' AFTER employment_status;

-- Add monthly salary
ALTER TABLE customers
ADD COLUMN monthly_salary DECIMAL(10,2) NOT NULL DEFAULT 0.00 AFTER account_type;


show tables;

describe customers;
describe bank_staff;
describe transactions;
describe atm_balance;
