# Stage 1: Build file jar bằng Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copy file pom.xml và source code vào container
COPY pom.xml .
COPY src ./src
# Build dự án, bỏ qua chạy test để tiết kiệm thời gian
RUN mvn clean package -DskipTests

# Stage 2: Tạo image chạy ứng dụng (Runtime)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copy file jar từ stage build sang stage này
COPY --from=build /app/target/*.jar app.jar

# Cấu hình cổng chạy (trùng với server.port trong application.properties)
EXPOSE 8080

# Lệnh khởi chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]