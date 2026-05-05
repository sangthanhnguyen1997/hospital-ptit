package ptit.hospitalmanagementsystem.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ptit.hospitalmanagementsystem.entity.*;
import ptit.hospitalmanagementsystem.enums.Role;
import ptit.hospitalmanagementsystem.repository.*;

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
            HealthcareCenterRepository centerRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {
        return args -> {
            // 1. Lấy hoặc tạo Cơ sở y tế mặc định
            HealthcareCenter defaultCenter = centerRepository.findById(1L).orElseGet(() ->
                    centerRepository.save(HealthcareCenter.builder()
                            .centerName("Bệnh viện Đa khoa PTIT")
                            .address("Km10, Đường Nguyễn Trãi, Hà Đông, Hà Nội")
                            .isActive(true)
                            .build())
            );

            // 2. Khởi tạo ADMIN (Giữ nguyên logic của bạn)
            if (userRepository.findByUsername("admin").isEmpty()) {
                Staff adminStaff = Staff.builder()
                        .fullName("Quản Trị Viên")
                        .position("ADMINISTRATOR")
                        .healthcareCenter(defaultCenter)
                        .isActive(true)
                        .user(User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin123"))
                                .roles(Role.ADMIN)
                                .fullName("Hệ Thống")
                                .build())
                        .build();
                staffRepository.save(adminStaff);
                log.info(">>> Đã tạo tài khoản ADMIN (admin/admin123)");
            }

            // 3. Khởi tạo RECEPTIONIST (Lễ tân - Thuộc Staff)
            if (userRepository.findByUsername("receptionist").isEmpty()) {
                Staff receptionist = Staff.builder()
                        .fullName("Nguyễn Văn Lễ Tân")
                        .position("RECEPTIONIST")
                        .healthcareCenter(defaultCenter)
                        .isActive(true)
                        .user(User.builder()
                                .username("receptionist")
                                .password(passwordEncoder.encode("staff123"))
                                .roles(Role.STAFF) // Giả định Role Enum của bạn có STAFF
                                .fullName("Lễ Tân 01")
                                .build())
                        .build();
                staffRepository.save(receptionist);
                log.info(">>> Đã tạo tài khoản RECEPTIONIST (receptionist/staff123)");
            }

            // 4. Khởi tạo DOCTOR (Bác sĩ - Class riêng)
            if (userRepository.findByUsername("doctor").isEmpty()) {
                Doctor doctor = Doctor.builder()
                        .fullName("BS. Nguyễn Văn mùi")
                        .specialty("Đa khoa")
                        .healthcareCenter(defaultCenter)
                        .isActive(true)
                        .user(User.builder()
                                .username("doctor")
                                .password(passwordEncoder.encode("doctor123"))
                                .roles(Role.DOCTOR) // Giả định có Role.DOCTOR
                                .fullName("Bác sĩ Sáng")
                                .build())
                        .build();
                doctorRepository.save(doctor);
                log.info(">>> Đã tạo tài khoản DOCTOR (doctor/doctor123)");
            }

            // 5. Khởi tạo PATIENT (Bệnh nhân)
            if (userRepository.findByUsername("patient").isEmpty()) {
                Patient patient = Patient.builder()
                        .fullName("Trần Văn Bệnh Nhân")
                        .address("Hà Đông, Hà Nội")
                        .phoneNumber("0988123456")
                        .user(User.builder()
                                .username("patient")
                                .password(passwordEncoder.encode("user123"))
                                .roles(Role.USER) // Bệnh nhân thường mang Role.USER
                                .fullName("Bệnh Nhân Test")
                                .build())
                        .build();
                patientRepository.save(patient);
                log.info(">>> Đã tạo tài khoản PATIENT (patient/user123)");
            }
        };
    }
}