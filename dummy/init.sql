USE hhplus;

-- users 테이블 생성
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY,
    user_name VARCHAR(255),
    point_balance DECIMAL(19, 2),
    created_at DATETIME,
    updated_at DATETIME
);

-- concert 테이블 생성
CREATE TABLE IF NOT EXISTS concert (
    concert_id BIGINT PRIMARY KEY,
    concert_name VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);

-- schedule 테이블 생성
CREATE TABLE IF NOT EXISTS schedule (
    schedule_id BIGINT PRIMARY KEY,
    concert_id BIGINT,
    price DECIMAL(19, 2),
    concert_date_time DATETIME,
    booking_start DATETIME,
    booking_end DATETIME,
    remaining_ticket DECIMAL(19, 2),
    total_ticket DECIMAL(19, 2),
    created_at DATETIME,
    updated_at DATETIME
);

-- seat 테이블 생성
CREATE TABLE IF NOT EXISTS seat (
    seat_id BIGINT PRIMARY KEY,
    schedule_id BIGINT,
    seat_number INT,
    seat_status ENUM('AVAILABLE', 'OCCUPIED'),
    seat_price DECIMAL(19, 2),
    created_at DATETIME,
    updated_at DATETIME
);

-- users 테이블에 CSV 데이터 로드
LOAD DATA INFILE '/dummy/csv/users.csv'
INTO TABLE users
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- concert 테이블에 CSV 데이터 로드
LOAD DATA INFILE '/dummy/csv/concert.csv'
INTO TABLE concert
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- schedule 테이블에 CSV 데이터 로드
LOAD DATA INFILE '/dummy/csv/schedule.csv'
INTO TABLE schedule
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- seat 테이블에 CSV 데이터 로드
LOAD DATA INFILE '/dummy/csv/seat.csv'
INTO TABLE seat
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;