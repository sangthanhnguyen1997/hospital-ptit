package ptit.hospitalmanagementsystem.configuration;


//xu ly loi 401


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import ptit.hospitalmanagementsystem.dto.ApiResponse;
import ptit.hospitalmanagementsystem.exception.ErrorCode;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // 1. Lấy định nghĩa lỗi 401 từ Enum
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        // 2. Set Header: Trả về mã 401 và kiểu dữ liệu là JSON
        response.setStatus(errorCode.getHttpStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 3. Đóng gói body lỗi vào ApiResponse
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        // 4. Biến Object thành chuỗi JSON và ghi vào Response trả về Client
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));

        // Đẩy dữ liệu đi
        response.flushBuffer();
    }
}