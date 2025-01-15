INSERT INTO users (user_id, user_name, point_Balance, created_at, updated_at) VALUES
(1, 'Alice', 100000.00, '2025-01-01 10:00:00', '2025-01-01 10:00:00'),
(2, 'Bob', 50000.50, '2025-01-02 11:30:00', '2025-01-02 11:30:00'),
(3, 'Charlie', 150000.75, '2025-01-03 14:00:00', '2025-01-03 14:00:00'),
(4, 'David', 75000.25, '2025-01-04 09:45:00', '2025-01-04 09:45:00'),
(5, 'Eve', 200000.00, '2025-01-05 12:15:00', '2025-01-05 12:15:00'),
(6, 'Frank', 30000.10, '2025-01-06 16:50:00', '2025-01-06 16:50:00'),
(7, 'Grace', 120000.00, '2025-01-07 08:20:00', '2025-01-07 08:20:00'),
(8, 'Hank', 95000.50, '2025-01-08 13:10:00', '2025-01-08 13:10:00'),
(9, 'Ivy', 40000.00, '2025-01-09 17:40:00', '2025-01-09 17:40:00'),
(10, 'Jack', 180000.30, '2025-01-10 19:30:00', '2025-01-10 19:30:00'),
(11, 'Kathy', 95000.75, '2025-01-11 10:00:00', '2025-01-11 10:00:00'),
(12, 'Leo', 82000.40, '2025-01-12 11:15:00', '2025-01-12 11:15:00'),
(13, 'Mona', 135000.00, '2025-01-13 14:25:00', '2025-01-13 14:25:00'),
(14, 'Nina', 73000.20, '2025-01-14 09:50:00', '2025-01-14 09:50:00'),
(15, 'Oscar', 210000.00, '2025-01-15 12:30:00', '2025-01-15 12:30:00'),
(16, 'Paul', 48000.55, '2025-01-16 16:40:00', '2025-01-16 16:40:00'),
(17, 'Quinn', 105000.00, '2025-01-17 08:10:00', '2025-01-17 08:10:00'),
(18, 'Rose', 89000.30, '2025-01-18 13:05:00', '2025-01-18 13:05:00'),
(19, 'Steve', 35000.00, '2025-01-19 17:25:00', '2025-01-19 17:25:00'),
(20, 'Tina', 170000.80, '2025-01-20 19:45:00', '2025-01-20 19:45:00');

INSERT INTO concert (concert_id, concert_name, created_at, updated_at) VALUES
(1, 'Rock Festival 2025', '2025-01-01 10:00:00', '2025-01-01 10:00:00'),
(2, 'Jazz Night', '2025-01-02 15:30:00', '2025-01-02 15:30:00'),
(3, 'Classical Evening', '2025-01-03 18:00:00', '2025-01-03 18:00:00'),
(4, 'Pop Extravaganza', '2025-01-04 20:00:00', '2025-01-04 20:00:00'),
(5, 'Indie Rock Session', '2025-01-05 14:45:00', '2025-01-05 14:45:00');

INSERT INTO schedule (schedule_id, concert_id, price, concert_date_time, booking_start, booking_end, remaining_ticket, total_ticket, created_at, updated_at) VALUES
(1, 1, 50000.00, '2025-02-01 18:00:00', '2025-01-01 10:00:00', '2025-01-31 23:59:59', 200, 300, '2025-01-01 10:00:00', '2025-01-01 10:00:00'),
(2, 1, 60000.00, '2025-02-02 19:00:00', '2025-01-02 10:00:00', '2025-02-01 23:59:59', 150, 300, '2025-01-02 10:00:00', '2025-01-02 10:00:00'),
(3, 1, 70000.00, '2025-02-03 20:00:00', '2025-01-03 10:00:00', '2025-02-02 23:59:59', 100, 300, '2025-01-03 10:00:00', '2025-01-03 10:00:00'),
(4, 1, 80000.00, '2025-02-04 20:30:00', '2025-01-04 10:00:00', '2025-02-03 23:59:59', 50, 300, '2025-01-04 10:00:00', '2025-01-04 10:00:00'),
(5, 1, 90000.00, '2025-02-05 21:00:00', '2025-01-05 10:00:00', '2025-02-04 23:59:59', 0, 300, '2025-01-05 10:00:00', '2025-01-05 10:00:00');

INSERT INTO seat (seat_id, schedule_id, seat_number, seat_status, seat_price, created_at, updated_at) VALUES
(1, 1, 1, 'AVAILABLE', 50000.00, '2025-01-01 10:00:00', '2025-01-01 10:00:00'),
(2, 1, 2, 'OCCUPIED', 50000.00, '2025-01-01 10:00:00', '2025-01-01 10:00:00'),
(3, 1, 3, 'AVAILABLE', 50000.00, '2025-01-01 10:00:00', '2025-01-01 10:00:00'),
(4, 1, 4, 'AVAILABLE', 60000.00, '2025-01-01 10:00:00', '2025-01-01 10:00:00'),
(5, 1, 5, 'OCCUPIED', 60000.00, '2025-01-01 10:00:00', '2025-01-01 10:00:00');
