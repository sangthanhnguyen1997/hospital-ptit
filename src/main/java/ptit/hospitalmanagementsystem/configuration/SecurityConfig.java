package ptit.hospitalmanagementsystem.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Cho phép dùng @PreAuthorize("hasRole('...')") ở Controller
public class SecurityConfig {

    // Danh sách các API "mở cửa" hoàn toàn, không cần Token
    private final String[] PUBLIC_ENDPOINTS = {
            "/users",
            "/auth/token",
            "/auth/login",
            "/auth/introspect",
            "/auth/refresh",
            "/auth/logout",

    };
    private final String[] SWAGGER_WHITELIST = {
            // 1. OpenAPI JSON (Nơi chứa cấu hình toàn bộ API của bạn)
            "/v3/api-docs",
            "/v3/api-docs/**",

            // 2. Swagger UI (Giao diện web xanh xanh)
            "/swagger-ui.html",
            "/swagger-ui/**",

            // 3. Swagger Resources (Cấu hình cho giao diện)
            "/swagger-resources",
            "/swagger-resources/**",

            // 4. Webjars (Chứa các file static như CSS, JS, hình ảnh)
            "/webjars/**",

            // 5. Thêm endpoint này nếu bạn dùng thư viện cũ hoặc cấu hình redirect
            "/configuration/ui",
            "/configuration/security"
    };


    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // --- BƯỚC 1: CẤU HÌNH PHÂN QUYỀN ĐƯỜNG DẪN ---
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll() // Cho phép POST vào các link public
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                 .anyRequest().authenticated()); // Các request còn lại đều phải đăng nhập (có Token)

        // --- BƯỚC 2: CẤU HÌNH RESOURCE SERVER (XỬ LÝ JWT) ---
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder) // Sử dụng bộ giải mã tùy chỉnh (để check Blacklist/Logout)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())) // Chuyển đổi Claim trong JWT thành Quyền hạn

                        // Xử lý khi xác thực thất bại (Sai token, hết hạn...)
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        // --- BƯỚC 3: TẮT CSRF ---
        // dùng JWT (Stateless), không dùng Session/Cookie nên không sợ tấn công CSRF
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    /**
     * CUSTOM QUYỀN HẠN (AUTHORITIES):
     * Mặc định Spring Security coi các claim "scope" là quyền hạn và thêm tiền tố "SCOPE_".
     * Ở đây ta chỉnh lại để nó khớp với các Role (ví dụ: ROLE_ADMIN)
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        //đã thêm "ROLE_" trong buildScope ở Service , ở đây ta để trống
        // để tránh bị trùng lặp thành ROLE_ROLE_ADMIN.
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
