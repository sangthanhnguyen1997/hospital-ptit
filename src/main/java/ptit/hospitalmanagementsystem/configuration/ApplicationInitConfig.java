package ptit.hospitalmanagementsystem.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ptit.hospitalmanagementsystem.entity.HealthcareCenter;
import ptit.hospitalmanagementsystem.entity.Staff;
import ptit.hospitalmanagementsystem.entity.User;
import ptit.hospitalmanagementsystem.enums.Role;
import ptit.hospitalmanagementsystem.repository.HealthcareCenterRepository;
import ptit.hospitalmanagementsystem.repository.StaffRepository;
import ptit.hospitalmanagementsystem.repository.UserRepository;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            StaffRepository staffRepository,
            HealthcareCenterRepository centerRepository) {
        return args -> {
            // 1. Khởi tạo Cơ sở y tế mặc định (nếu chưa có) vì Staff/Doctor cần có mã cơ sở
            HealthcareCenter defaultCenter = centerRepository.findById(1L).orElseGet(() ->
                    centerRepository.save(HealthcareCenter.builder()
                            .centerName("Bệnh viện Đa khoa PTIT")
                            .address("Km10, Đường Nguyễn Trãi, Hà Đông, Hà Nội")
                            .isActive(true)
                            .build())
            );

            // 2. Kiểm tra nếu chưa có tài khoản admin
            if (userRepository.findByUsername("admin").isEmpty()) {

                // Tạo đối tượng User Admin
                User adminUser = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .roles(Role.ADMIN) // Role Enum bạn đã có
                        .fullName("Hệ Thống")
                        .build();

                // Tạo đối tượng Staff tương ứng với Admin
                Staff adminStaff = Staff.builder()
                        .fullName("Quản Trị Viên Hệ Thống")
                        .position("ADMINISTRATOR")
                        .healthcareCenter(defaultCenter) // Gán vào cơ sở y tế vừa tạo
                        .isActive(true)
                        .user(adminUser) // Mapping 1-1 chuẩn ORM
                        .build();

                // Nhờ CascadeType.ALL đã cấu hình trong Entity, chỉ cần save Staff là User sẽ được save theo
                staffRepository.save(adminStaff);

                log.warn(">>> TÀI KHOẢN ADMIN ĐÃ ĐƯỢC KHỞI TẠO <<<");
                log.warn("Username: admin / Password: admin123");
            }
        };
    }
}