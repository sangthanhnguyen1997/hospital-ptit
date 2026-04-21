# Hospital Management System

Dự án Spring Boot kết nối PostgreSQL.

## Mô hình chạy

* PostgreSQL chạy bằng Docker
* Spring Boot chạy bằng Maven

---

## Yêu cầu môi trường

Cần cài đặt:

* Java JDK 17+
* Maven
* Docker + Docker Compose

Kiểm tra:

```bash
java -version
mvn -version
docker --version
docker compose version
```

---

# 🚀 CÁC BƯỚC CHẠY PROJECT

## 🔹 Bước 1: Mở terminal và đi tới thư mục project

```bash
cd hospital-management
```

👉 Quan trọng: phải đứng tại thư mục chứa:

* `pom.xml`
* `docker-compose.yml`

---

## 🔹 Bước 2: Chạy database PostgreSQL bằng Docker

```bash
docker compose up -d
```

Kiểm tra database:

```bash
docker compose ps
```

👉 Nếu thấy `hospital-postgres` đang chạy là OK

---

## 🔹 Bước 3: Chạy ứng dụng Spring Boot

Vẫn đứng tại thư mục project:

### Cách 1: dùng Maven

```bash
mvn spring-boot:run
```

### Cách 2: dùng Maven Wrapper (IntelliJ)

Windows:

```bash
mvnw.cmd spring-boot:run
```

---

## 🔹 Thứ tự chạy (rất quan trọng)

Luôn chạy theo đúng thứ tự:

```bash
cd hospital-management
docker compose up -d
mvn spring-boot:run
```

---