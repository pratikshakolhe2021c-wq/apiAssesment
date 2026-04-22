-- Sample Data for Student Management System

-- Insert Sample Students
INSERT INTO students (student_name, student_id, grade, mobile_number, school_name, active) VALUES
('John Doe', 'STU001', 'Grade 10', '+1234567890', 'Springfield High School', true),
('Jane Smith', 'STU002', 'Grade 9', '+1234567891', 'Springfield High School', true),
('Michael Johnson', 'STU003', 'Grade 10', '+1234567892', 'Lincoln High School', true),
('Emily Davis', 'STU004', 'Grade 8', '+1234567893', 'Springfield High School', true),
('Robert Wilson', 'STU005', 'Grade 11', '+1234567894', 'Lincoln High School', true),
('Sarah Brown', 'STU006', 'Grade 9', '+1234567895', 'Washington Academy', true),
('James Taylor', 'STU007', 'Grade 12', '+1234567896', 'Springfield High School', true),
('Lisa Anderson', 'STU008', 'Grade 7', '+1234567897', 'Washington Academy', true);

-- Insert Sample Receipts
INSERT INTO receipts (receipt_number, student_id, student_name, fee_type, amount, payment_date, payment_method, transaction_id, academic_year, month, quarter, remarks, created_at, created_by) VALUES
('REC20240101001', 1, 'John Doe', 'Tuition Fee', 500.00, '2024-01-15 10:30:00', 'Cash', 'TXN001', '2024-2025', 'January', 'Q1', 'January Tuition Fee', '2024-01-15 10:30:00', 'Admin'),
('REC20240201001', 2, 'Jane Smith', 'Tuition Fee', 450.00, '2024-02-10 14:20:00', 'Bank Transfer', 'TXN002', '2024-2025', 'February', 'Q1', 'February Tuition Fee', '2024-02-10 14:20:00', 'Admin'),
('REC20240301001', 1, 'John Doe', 'Lab Fee', 100.00, '2024-03-05 09:15:00', 'Credit Card', 'TXN003', '2024-2025', 'March', 'Q1', 'Science Lab Fee', '2024-03-05 09:15:00', 'Admin'),
('REC20240401001', 3, 'Michael Johnson', 'Tuition Fee', 550.00, '2024-04-12 11:45:00', 'Cash', 'TXN004', '2024-2025', 'April', 'Q2', 'April Tuition Fee', '2024-04-12 11:45:00', 'Admin'),
('REC20240501001', 4, 'Emily Davis', 'Library Fee', 50.00, '2024-05-08 16:30:00', 'Bank Transfer', 'TXN005', '2024-2025', 'May', 'Q2', 'Library Annual Fee', '2024-05-08 16:30:00', 'Admin'),
('REC20240601001', 5, 'Robert Wilson', 'Tuition Fee', 600.00, '2024-06-20 13:10:00', 'Credit Card', 'TXN006', '2024-2025', 'June', 'Q2', 'June Tuition Fee', '2024-06-20 13:10:00', 'Admin'),
('REC20240701001', 2, 'Jane Smith', 'Sports Fee', 75.00, '2024-07-15 10:00:00', 'Cash', 'TXN007', '2024-2025', 'July', 'Q3', 'Sports Annual Fee', '2024-07-15 10:00:00', 'Admin'),
('REC20240801001', 6, 'Sarah Brown', 'Tuition Fee', 425.00, '2024-08-22 15:30:00', 'Bank Transfer', 'TXN008', '2024-2025', 'August', 'Q3', 'August Tuition Fee', '2024-08-22 15:30:00', 'Admin');
