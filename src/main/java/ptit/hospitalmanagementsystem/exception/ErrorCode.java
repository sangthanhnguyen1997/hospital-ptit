package ptit.hospitalmanagementsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * Enum quản lý tập trung các mã lỗi, thông báo lỗi và mã HTTP tương ứng.
 */
@Getter
public enum ErrorCode {
    // 1. Lỗi hệ thống: Khi có lỗi phát sinh mà ta chưa lường trước được
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),

    // 2. Lỗi cấu hình Validation: Khi Key thông báo trong @Valid không khớp với Enum này
    INVALID_KEY(1001, "Invalid message Key", HttpStatus.BAD_REQUEST),

    // 3. Các lỗi nghiệp vụ về User
    USER_EXISTED(1002, "User already exists", HttpStatus.CONFLICT),
    USER_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User Not Existed", HttpStatus.NOT_FOUND),

    // 4. Lỗi Bảo mật (401 Unauthorized): Sai thông tin đăng nhập hoặc Token hết hạn
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),

    // 5. Lỗi Phân quyền (403 Forbidden): Đã đăng nhập nhưng không đủ quyền truy cập
    UNAUTHORIZED(1007, "You do not have permission.", HttpStatus.FORBIDDEN),

    // 6. Lỗi Custom Validation: Kiểm tra tuổi (Đặc biệt có placeholder {min} để map dữ liệu động)
    INVALID_DOB(1008, "You must be at least {min} years old", HttpStatus.BAD_REQUEST),
    ;

    /**
     * Constructor khởi tạo Enum
     */
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = statusCode;
    }

    private int code;           // Mã lỗi nội bộ để phía Frontend dễ xử lý
    private String message;     // Thông báo chi tiết cho người dùng
    private HttpStatusCode httpStatusCode; // Mã HTTP chuẩn để trình duyệt/client hiểu
}