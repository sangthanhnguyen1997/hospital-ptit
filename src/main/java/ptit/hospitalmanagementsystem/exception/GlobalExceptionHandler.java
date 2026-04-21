package ptit.hospitalmanagementsystem.exception;


import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ptit.hospitalmanagementsystem.dto.ApiResponse;

import java.util.Map;
import java.util.Objects;

/**
 * @RestControllerAdvice: Kết hợp giữa @ControllerAdvice và @ResponseBody.
 * Giúp bắt các Exception ném ra từ các Controller và trả về JSON thay vì trang lỗi HTML.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Tên thuộc tính cấu hình trong Annotation (ví dụ: min trong @DobConstraint(min=18))
    private static final String MIN_ATTRIBUTE = "min";

    /**
     * 1. Xử lý lỗi Runtime chung.
     * Dùng để "bọc" những lỗi không mong muốn (Lỗi NullPointer, lỗi Logic chưa bắt...).
     */
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    /**
     * 2. Xử lý AppException.
     * Đây là lỗi "chủ động": Khi bạn ném 'throw new AppException(ErrorCode.USER_EXISTED)'.
     */
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    /**
     * 3. Xử lý lỗi Phân Quyền (403 Forbidden).
     * Xảy ra khi dùng @PreAuthorize("hasRole('ADMIN')") mà User chỉ có quyền USER.
     */
    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse> handlingAuthorizationDeniedException(AuthorizationDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    /**
     * 4. Xử lý lỗi Validation (MethodArgumentNotValidException).
     * Xảy ra khi dữ liệu Client gửi lên vi phạm các @Valid (như @Size, @DobConstraint...).
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        // Bước A: Lấy 'message' key từ Annotation (ví dụ lấy được chuỗi "INVALID_DOB")
        String enumKey = exception.getBindingResult().getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY; // Mặc định nếu không tìm thấy Enum
        Map<String, Object> attributes = null;

        try {
            // Bước B: Dùng Reflection (valueOf) để tìm Enum ErrorCode tương ứng với chuỗi enumKey
            errorCode = ErrorCode.valueOf(enumKey);

            /**
             * Bước C: Trích xuất các thuộc tính (attributes) từ Annotation.
             * Ví dụ: @DobConstraint(min = 18), ta muốn lấy con số 18 này ra.
             */
            var constraintViolations = exception.getBindingResult()
                    .getAllErrors().getFirst() // Lấy lỗi đầu tiên gặp phải
                    .unwrap(ConstraintViolation.class); // "Mở gói" để truy cập sâu vào thông tin lỗi

            // Lấy toàn bộ các tham số khai báo trong Annotation (min, message, payload...)
            attributes = constraintViolations.getConstraintDescriptor().getAttributes();
            log.info("Annotation attributes: {}", attributes.toString());

        } catch (IllegalArgumentException e) {
            // Catch này chạy khi enumKey gửi về không khớp với bất kỳ Enum nào trong ErrorCode
        }

        // Bước D: Trả về Response. Nếu có attributes thì tiến hành map dữ liệu động vào message.
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage())
                .build());
    }

    /**
     * Hàm phụ trợ: Thay thế placeholder {min} trong chuỗi message bằng giá trị thực tế.
     * Ví dụ: "Tuổi tối thiểu là {min}" -> "Tuổi tối thiểu là 18"
     */
    private String mapAttribute(String message, Map<String, Object> attributes) {
        // Lấy giá trị 'min' từ map attributes của Annotation
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        // Thay thế chuỗi "{min}" trong ErrorCode bằng giá trị thực
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}