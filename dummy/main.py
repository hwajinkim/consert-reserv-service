import csv
from datetime import datetime, timedelta
import random
from faker import Faker
import numpy as np

fake = Faker("en_US")
Faker.seed(42)
random.seed(42)


# 1. 사용자 데이터 생성 (10만명)
def generate_users(num_users):
    with open("users.csv", "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(
            ["user_id", "user_name", "point_balance", "created_at", "updated_at"]
        )

        for user_id in range(1, num_users + 1):
            created = fake.date_time_between(start_date="-5y", end_date="now")
            updated = fake.date_time_between(start_date=created, end_date="now")

            writer.writerow(
                [
                    user_id,
                    fake.name(),
                    f"{random.randint(0, 1_000_000)}.00",
                    created.strftime("%Y-%m-%d %H:%M:%S"),
                    updated.strftime("%Y-%m-%d %H:%M:%S"),
                ]
            )


# 2. 콘서트 데이터 생성 (1,000개)
def generate_concerts(num_concerts):
    with open("concerts.csv", "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(["concert_id", "concert_name", "created_at", "updated_at"])

        for concert_id in range(1, num_concerts + 1):
            name = f"{fake.word().title()} {random.choice(["Festival", "Carnival", "Concert", "Jam", "Show", "Gala"])} {random.randint(2024, 2026)}"
            created = fake.date_time_between(start_date="-5y", end_date="now")
            updated = fake.date_time_between(start_date=created, end_date="now")

            writer.writerow(
                [
                    concert_id,
                    name,
                    created.strftime("%Y-%m-%d %H:%M:%S"),
                    updated.strftime("%Y-%m-%d %H:%M:%S"),
                ]
            )


# 3. 스케줄 데이터 생성 (5,000개)
def generate_schedules(num_concerts):
    with open("schedules.csv", "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(
            [
                "schedule_id",
                "concert_id",
                "price",
                "concert_date_time",
                "booking_start",
                "booking_end",
                "remaining_ticket",
                "total_ticket",
                "created_at",
                "updated_at",
            ]
        )

        schedule_id = 1
        for concert_id in range(1, num_concerts + 1):
            # 콘서트당 5개의 스케줄 생성
            num_schedules = 5
            total_tickets = random.randint(100, 100_000)
            weights = np.random.dirichlet(np.ones(num_schedules))  # 티켓 분배 가중치

            # 시간 중복 방지를 위한 타임라인 관리
            prev_end = datetime(2025, 1, 1)
            for idx in range(num_schedules):
                # 콘서트 날짜 생성 (2025년)
                concert_date = fake.date_time_between(
                    start_date=datetime(2025, 1, 1), end_date=datetime(2025, 12, 31)
                )

                # 예매 기간 생성 (콘서트 날짜 이전)
                booking_start = fake.date_time_between(
                    start_date=prev_end + timedelta(hours=1),
                    end_date=concert_date - timedelta(days=1),
                )
                booking_end = fake.date_time_between(
                    start_date=booking_start, end_date=concert_date - timedelta(hours=1)
                )

                # 티켓 분배
                total = int(total_tickets * weights[idx])
                remaining = random.randint(0, total)

                created = fake.date_time_between(start_date="-5y", end_date="now")
                updated = fake.date_time_between(start_date=created, end_date="now")

                writer.writerow(
                    [
                        schedule_id,
                        concert_id,
                        f"{random.randint(1_000, 1_000_000)}.00",
                        concert_date.strftime("%Y-%m-%d %H:%M:%S"),
                        booking_start.strftime("%Y-%m-%d %H:%M:%S"),
                        booking_end.strftime("%Y-%m-%d %H:%M:%S"),
                        f"{remaining}.00",
                        f"{total}.00",
                        created.strftime("%Y-%m-%d %H:%M:%S"),
                        updated.strftime("%Y-%m-%d %H:%M:%S"),
                    ]
                )
                schedule_id += 1
                prev_end = booking_end  # 다음 스케줄 시작 시간 업데이트


# 4. 좌석 데이터 생성 (50만개)
def generate_seats(num_concerts, num_seats):
    with open("seats.csv", "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(
            [
                "seat_id",
                "schedule_id",
                "seat_number",
                "seat_status",
                "seat_price",
                "created_at",
                "updated_at",
            ]
        )

        seat_id = 1
        for concert_id in range(1, num_concerts + 1):
            for schedule_id in range(1, 6):
                for seat_num in range(1, 11):
                    # 스케줄당 10개의 좌석 생성
                    status = random.choice(["OCCUPIED", "AVAILABLE"])
                    created = fake.date_time_between(start_date="-5y", end_date="now")
                    updated = fake.date_time_between(start_date=created, end_date="now")

                    writer.writerow(
                        [
                            seat_id,
                            schedule_id,
                            f"{seat_num + 10 * (schedule_id - 1)}",
                            status,
                            f"{random.randint(1_000, 1_000_000)}.00",
                            created.strftime("%Y-%m-%d %H:%M:%S"),
                            updated.strftime("%Y-%m-%d %H:%M:%S"),
                        ]
                    )
                    seat_id += 1
                    if seat_id > num_seats:
                        return


# 실행부
if __name__ == "__main__":
    num_users = 100_000
    num_concerts = 1_000
    num_seats = 500_000
    generate_users(num_users)  # 사용자 10만명
    generate_concerts(num_concerts)  # 콘서트 1,000개
    generate_schedules(num_concerts)  # 스케줄 5,000개 (1,000개 콘서트 * 5개)
    generate_seats(num_seats, num_seats)  # 좌석 50만개
