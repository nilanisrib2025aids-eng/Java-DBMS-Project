-- ====================================================================
-- E-Governance Grievance Redressal & Escalation Workflow System Schema
-- Database: egovernance_workflow_db
-- ====================================================================

CREATE DATABASE IF NOT EXISTS egovernance_workflow_db;
USE egovernance_workflow_db;

-- Drop in reverse dependency order for clean recreation if re-run
DROP TABLE IF EXISTS journal_entries;
DROP TABLE IF EXISTS journals;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS invoices;
DROP TABLE IF EXISTS vendor_bills;
DROP TABLE IF EXISTS purchase_orders;
DROP TABLE IF EXISTS grievances;
DROP TABLE IF EXISTS chart_of_accounts;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS contacts;

-- 1. Contact Master (Citizen, Civil Works Contractor, Municipal Utility)
CREATE TABLE contacts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    contact_type ENUM('CITIZEN', 'CONTRACTOR', 'UTILITY') NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Department / Analytic Account Master
CREATE TABLE departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) UNIQUE NOT NULL,
    head_officer VARCHAR(100),
    allocated_budget DECIMAL(15,2) DEFAULT 0.00
);

-- 3. Product / Service Master (Road Repair, Water Pipe Replacement, etc.)
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    product_type ENUM('SERVICE', 'GOODS') NOT NULL,
    standard_rate DECIMAL(15,2) DEFAULT 0.00,
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL
);

-- 4. Chart of Accounts Master
CREATE TABLE chart_of_accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    account_type ENUM('ASSET', 'LIABILITY', 'INCOME', 'EXPENSE') NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00
);

-- 5. Grievances Module (72-hour SLA window & Escalation workflow)
CREATE TABLE grievances (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_number VARCHAR(50) UNIQUE NOT NULL,
    citizen_id INT NOT NULL,
    department_id INT NOT NULL,
    product_id INT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    status ENUM('SUBMITTED', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') DEFAULT 'SUBMITTED',
    escalation_status ENUM('NORMAL', 'ESCALATED') DEFAULT 'NORMAL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sla_deadline TIMESTAMP NOT NULL,
    resolved_at TIMESTAMP NULL,
    resolution_notes TEXT,
    FOREIGN KEY (citizen_id) REFERENCES contacts(id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 6. Purchase Orders (Repair Contractor POs)
CREATE TABLE purchase_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    po_number VARCHAR(50) UNIQUE NOT NULL,
    grievance_id INT,
    contractor_id INT NOT NULL,
    department_id INT NOT NULL,
    product_id INT,
    amount DECIMAL(15,2) NOT NULL,
    status ENUM('DRAFT', 'CONFIRMED', 'BILLED', 'CANCELLED') DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (grievance_id) REFERENCES grievances(id),
    FOREIGN KEY (contractor_id) REFERENCES contacts(id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 7. Vendor Bills (Contractor repair invoices)
CREATE TABLE vendor_bills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bill_number VARCHAR(50) UNIQUE NOT NULL,
    po_id INT NOT NULL,
    contractor_id INT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    status ENUM('POSTED', 'PAID') DEFAULT 'POSTED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (po_id) REFERENCES purchase_orders(id),
    FOREIGN KEY (contractor_id) REFERENCES contacts(id)
);

-- 8. Customer Invoices / Permit Fees (Civic service fee order)
CREATE TABLE invoices (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_number VARCHAR(50) UNIQUE NOT NULL,
    citizen_id INT NOT NULL,
    department_id INT NOT NULL,
    fee_type VARCHAR(100) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    status ENUM('UNPAID', 'PAID') DEFAULT 'UNPAID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (citizen_id) REFERENCES contacts(id),
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- 9. Payments (Contractor disbursements and permit fee collections)
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    payment_number VARCHAR(50) UNIQUE NOT NULL,
    payment_type ENUM('PAYMENT_OUT', 'PAYMENT_IN') NOT NULL, -- OUT to Contractor, IN from Citizen
    reference_bill_id INT NULL,
    reference_invoice_id INT NULL,
    contact_id INT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50) DEFAULT 'Bank Transfer',
    FOREIGN KEY (contact_id) REFERENCES contacts(id)
);

-- 10. Journals & Journal Entries (Double Entry Bookkeeping)
CREATE TABLE journals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    journal_code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    journal_type ENUM('PURCHASE', 'FINES', 'BANK', 'GENERAL') NOT NULL
);

CREATE TABLE journal_entries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    entry_number VARCHAR(50) NOT NULL,
    journal_id INT NOT NULL,
    account_id INT NOT NULL,
    debit DECIMAL(15,2) DEFAULT 0.00,
    credit DECIMAL(15,2) DEFAULT 0.00,
    reference VARCHAR(100),
    description VARCHAR(255),
    entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (journal_id) REFERENCES journals(id),
    FOREIGN KEY (account_id) REFERENCES chart_of_accounts(id)
);

-- ====================================================================
-- Initial Seed Data
-- ====================================================================

-- Seed Chart of Accounts
INSERT INTO chart_of_accounts (id, code, name, account_type, balance) VALUES
(1, '1001', 'Municipal Treasury Fund (Bank)', 'ASSET', 5000000.00),
(2, '2001', 'Contractor Payables', 'LIABILITY', 0.00),
(3, '4001', 'Civic Fines & Permit Revenue', 'INCOME', 0.00),
(4, '5001', 'Civic Repair & Maintenance Expenses', 'EXPENSE', 0.00);

-- Seed Journals
INSERT INTO journals (id, journal_code, name, journal_type) VALUES
(1, 'PJ-01', 'Contractor Repair Purchase Journal', 'PURCHASE'),
(2, 'FJ-01', 'Civic Fines & Permit Revenue Journal', 'FINES'),
(3, 'BJ-01', 'Municipal Treasury Bank Journal', 'BANK');

-- Seed Departments (Analytic Accounts)
INSERT INTO departments (id, name, code, head_officer, allocated_budget) VALUES
(1, 'Public Works Department - Roads', 'PWD-ROADS', 'Eng. Rajesh Kumar', 1500000.00),
(2, 'Water Supply & Sewerage Board', 'WSSB', 'Eng. Meenakshi S', 1200000.00),
(3, 'Public Health & Sanitation', 'PHS', 'Dr. Arun Patel', 800000.00),
(4, 'Electrical & Street Lighting', 'ESL', 'Eng. Vikram Seth', 600000.00);

-- Seed Contacts
INSERT INTO contacts (id, name, contact_type, email, phone, address) VALUES
(1, 'Ramesh Sharma', 'CITIZEN', 'ramesh@example.com', '9876543210', 'Flat 402, Green Avenue, City'),
(2, 'Ananya Das', 'CITIZEN', 'ananya@example.com', '9876543211', '12 Lake View Road, City'),
(3, 'Apex Civil Infrastructure Ltd', 'CONTRACTOR', 'contact@apexcivil.com', '9876500001', 'Industrial Area, Phase 2'),
(4, 'Urban Flow Pipes & Fitting Corp', 'CONTRACTOR', 'sales@urbanflow.com', '9876500002', 'Sector 14, Commercial Hub'),
(5, 'State Power & Water Utility Board', 'UTILITY', 'utility@stategov.org', '9876500003', 'Govt Complex Block A');

-- Seed Products/Services
INSERT INTO products (id, name, product_type, standard_rate, department_id) VALUES
(1, 'Road Pothole & Bitumen Repair', 'SERVICE', 25000.00, 1),
(2, 'Underground Water Pipe Replacement', 'GOODS', 45000.00, 2),
(3, 'Drainage De-silting & Clearing', 'SERVICE', 15000.00, 3),
(4, 'LED Streetlight Pole Replacement', 'GOODS', 12000.00, 4);

-- Seed an Initial Grievance with deadline for immediate visualization
INSERT INTO grievances (id, ticket_number, citizen_id, department_id, product_id, title, description, location, status, escalation_status, created_at, sla_deadline) VALUES
(1, 'GRV-2026-0001', 1, 1, 1, 'Major Pothole on M.G. Road', 'Deep road pothole causing traffic jam and hazard near junction 4.', 'M.G. Road Junction 4', 'SUBMITTED', 'NORMAL', NOW(), DATE_ADD(NOW(), INTERVAL 72 HOUR));
