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
import ptit.hospitalmanagementsystem.enums.Gender;
import ptit.hospitalmanagementsystem.enums.Role;
import ptit.hospitalmanagementsystem.repository.*;

import java.math.BigDecimal;
import java.util.List;

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
            DoctorRepository doctorRepository,
            ExaminationRoomRepository roomRepository,
            MedicineRepository medicineRepository,
            TestCategoryRepository testCategoryRepository

    ) {
        return args -> {
            // 1. Khởi tạo 2 Cơ sở y tế
            HealthcareCenter haDongCenter = centerRepository.findById(1L).orElseGet(() ->
                    centerRepository.save(HealthcareCenter.builder()
                            .centerName("Bệnh viện Đa khoa Hà Đông")
                            .address("Số 2 Bế Văn Đàn, Quang Trung, Hà Đông, Hà Nội")
                            .phoneNumber("02433824216")
                            .isActive(true)
                            .build())
            );

            HealthcareCenter thanhNhanCenter = centerRepository.findById(2L).orElseGet(() ->
                    centerRepository.save(HealthcareCenter.builder()
                            .centerName("Bệnh viện Đa khoa Thanh Nhàn")
                            .address("Số 42 Phố Thanh Nhàn, Hai Bà Trưng, Hà Nội")
                            .phoneNumber("02439714363")
                            .isActive(true)
                            .build())
            );

            // 2. Khởi tạo Phòng khám cho cả 2 cơ sở (Nếu chưa có phòng nào)
            if (roomRepository.count() == 0) {
                initializeRooms(roomRepository, haDongCenter);
                initializeRooms(roomRepository, thanhNhanCenter);
            }

            // 3. Khởi tạo tài khoản nhân viên (Admin dùng chung, Lễ tân riêng từng nơi)
            initializeStaff(userRepository, staffRepository, haDongCenter, thanhNhanCenter);

            // 4. Khởi tạo Bác sĩ (Mặc định + 10 BS mẫu) cho Hà Đông
            if (doctorRepository.count() == 0) {
                initializeDoctors(userRepository, doctorRepository, haDongCenter);
            }

            // 5. Khởi tạo 20 Bệnh nhân (10 người cho mỗi cơ sở)
            if (patientRepository.count() <= 1) {
                initializePatients(userRepository, patientRepository, haDongCenter, "HD");
                initializePatients(userRepository, patientRepository, thanhNhanCenter, "TN");
            }
            // 6. tao danh muc thuoc va xet nghiem
            if (medicineRepository.count() == 0) {
                initializeMedicines(medicineRepository);
            }

            if (testCategoryRepository.count() == 0) {
                initializeTestCategories(testCategoryRepository);
            }

            log.info(">>> [SUCCESS] Toàn bộ dữ liệu hệ thống đã được Seed thành công!");
        };
    }

    private void initializeRooms(ExaminationRoomRepository roomRepository, HealthcareCenter center) {
        List<ExaminationRoom> rooms = List.of(
                ExaminationRoom.builder().roomName("Phòng khám Nội - " + center.getCenterName()).floor("Tầng 1").roomType("Nội khoa").healthcareCenter(center).isActive(true).build(),
                ExaminationRoom.builder().roomName("Phòng khám Ngoại - " + center.getCenterName()).floor("Tầng 1").roomType("Ngoại khoa").healthcareCenter(center).isActive(true).build(),
                ExaminationRoom.builder().roomName("Phòng Sản khoa - " + center.getCenterName()).floor("Tầng 2").roomType("Sản khoa").healthcareCenter(center).isActive(true).build(),
                ExaminationRoom.builder().roomName("Phòng Nhi khoa - " + center.getCenterName()).floor("Tầng 2").roomType("Nhi khoa").healthcareCenter(center).isActive(true).build(),
                ExaminationRoom.builder().roomName("Phòng Tai Mũi Họng - " + center.getCenterName()).floor("Tầng 3").roomType("Chuyên khoa").healthcareCenter(center).isActive(true).build(),
                ExaminationRoom.builder().roomName("Phòng Răng Hàm Mặt - " + center.getCenterName()).floor("Tầng 3").roomType("Chuyên khoa").healthcareCenter(center).isActive(true).build(),
                ExaminationRoom.builder().roomName("Phòng Mắt - " + center.getCenterName()).floor("Tầng 3").roomType("Chuyên khoa").healthcareCenter(center).isActive(true).build(),
                ExaminationRoom.builder().roomName("Phòng Da liễu - " + center.getCenterName()).floor("Tầng 4").roomType("Chuyên khoa").healthcareCenter(center).isActive(true).build(),
                ExaminationRoom.builder().roomName("Phòng Cấp cứu - " + center.getCenterName()).floor("Tầng 1").roomType("Cấp cứu").healthcareCenter(center).isActive(true).build(),
                ExaminationRoom.builder().roomName("Phòng Xét nghiệm - " + center.getCenterName()).floor("Tầng 1").roomType("Cận lâm sàng").healthcareCenter(center).isActive(true).build()
        );
        roomRepository.saveAll(rooms);
        log.info(">>> Đã khởi tạo 10 phòng khám tại {}", center.getCenterName());
    }

    private void initializeStaff(UserRepository userRepository, StaffRepository staffRepository, HealthcareCenter hd, HealthcareCenter tn) {
        // 1. ADMIN (Mặc định ở Hà Đông)
        if (userRepository.findByUsername("admin").isEmpty()) {
            staffRepository.save(Staff.builder()
                    .fullName("Quản Trị Viên")
                    .position("ADMINISTRATOR")
                    .healthcareCenter(hd)
                    .isActive(true)
                    .user(User.builder().username("admin").password(passwordEncoder.encode("admin123")).roles(Role.ADMIN).fullName("Hệ Thống").build())
                    .build());
            log.info(">>> Đã tạo tài khoản ADMIN (admin/admin123)");
        }

        // 2. Lễ tân Hà Đông
        if (userRepository.findByUsername("receptionist").isEmpty()) {
            staffRepository.save(Staff.builder()
                    .fullName("Lễ Tân Hà Đông")
                    .position("RECEPTIONIST")
                    .healthcareCenter(hd)
                    .isActive(true)
                    .user(User.builder().username("receptionist").password(passwordEncoder.encode("staff123")).roles(Role.STAFF).fullName("Lễ Tân HD").build())
                    .build());
            log.info(">>> Đã tạo tài khoản Lễ tân Hà Đông");
        }

        // 3. Lễ tân Thanh Nhàn
        if (userRepository.findByUsername("receptionist_tn").isEmpty()) {
            staffRepository.save(Staff.builder()
                    .fullName("Lễ Tân Thanh Nhàn")
                    .position("RECEPTIONIST")
                    .healthcareCenter(tn)
                    .isActive(true)
                    .user(User.builder().username("receptionist_tn").password(passwordEncoder.encode("staff123")).roles(Role.STAFF).fullName("Lễ Tân TN").build())
                    .build());
            log.info(">>> Đã tạo tài khoản Lễ tân Thanh Nhàn (receptionist_tn/staff123)");
        }
    }

    private void initializeDoctors(UserRepository userRepository, DoctorRepository doctorRepository, HealthcareCenter center) {
        // BS. Mùi mặc định
        if (userRepository.findByUsername("doctor").isEmpty()) {
            doctorRepository.save(Doctor.builder()
                    .fullName("BS. Nguyễn Văn Mùi")
                    .specialty("Đa khoa")
                    .gender(Gender.MALE)
                    .healthcareCenter(center)
                    .isActive(true)
                    .user(User.builder().username("doctor").password(passwordEncoder.encode("doctor123")).roles(Role.DOCTOR).fullName("Bác sĩ Mùi").build())
                    .build());
        }

        List<DoctorData> doctorsData = List.of(
                new DoctorData("le.minh@hospital.com", "0911222333", "BS. Lê Quang Minh", "Nội khoa", Gender.MALE, "CCHN-0001"),
                new DoctorData("pham.thanh@hospital.com", "0911222444", "BS. Phạm Thị Thanh", "Sản khoa", Gender.FEMALE, "CCHN-0002"),
                new DoctorData("nguyen.tuan@hospital.com", "0911222555", "BS. Nguyễn Anh Tuấn", "Ngoại khoa", Gender.MALE, "CCHN-0003"),
                new DoctorData("hoang.lan@hospital.com", "0911222666", "BS. Hoàng Bảo Lan", "Nhi khoa", Gender.FEMALE, "CCHN-0004"),
                new DoctorData("tran.duc@hospital.com", "0911222777", "BS. Trần Minh Đức", "Răng Hàm Mặt", Gender.MALE, "CCHN-0005"),
                new DoctorData("vu.huyen@hospital.com", "0911222888", "BS. Vũ Thu Huyền", "Tai Mũi Họng", Gender.FEMALE, "CCHN-0006"),
                new DoctorData("dang.nam@hospital.com", "0911222999", "BS. Đặng Tiến Nam", "Mắt", Gender.MALE, "CCHN-0007"),
                new DoctorData("do.thao@hospital.com", "0911333111", "BS. Đỗ Phương Thảo", "Da liễu", Gender.FEMALE, "CCHN-0008"),
                new DoctorData("bui.long@hospital.com", "0911333222", "BS. Bùi Hoàng Long", "Cấp cứu", Gender.MALE, "CCHN-0009"),
                new DoctorData("ngo.mai@hospital.com", "0911333333", "BS. Ngô Tuyết Mai", "Chẩn đoán hình ảnh", Gender.FEMALE, "CCHN-0010")
        );

        for (DoctorData data : doctorsData) {
            if (userRepository.findByUsername(data.email).isEmpty()) {
                doctorRepository.save(Doctor.builder()
                        .fullName(data.fullName).specialty(data.specialty).gender(data.gender)
                        .practicingCertificate(data.certificate).phoneNumber(data.phone).email(data.email)
                        .healthcareCenter(center).isActive(true)
                        .user(User.builder().username(data.email).password(passwordEncoder.encode(data.phone)).roles(Role.DOCTOR).fullName(data.fullName).build())
                        .build());
            }
        }
        log.info(">>> Đã khởi tạo 11 bác sĩ tại {}", center.getCenterName());
    }

    private void initializePatients(UserRepository userRepository, PatientRepository patientRepository, HealthcareCenter center, String prefix) {
        for (int i = 1; i <= 10; i++) {
            String phone = String.format("09%s00000%02d", prefix.equals("HD") ? "88" : "99", i);
            String username = "patient_" + prefix.toLowerCase() + "_" + i;

            if (userRepository.findByUsername(username).isEmpty()) {
                patientRepository.save(Patient.builder()
                        .fullName("Bệnh Nhân " + prefix + " " + i)
                        .address("Địa chỉ tại " + center.getCenterName())
                        .phoneNumber(phone)
                        .identityNumber("00109700" + prefix + i)
                        .user(User.builder().username(username).password(passwordEncoder.encode("user123")).roles(Role.USER).fullName("Bệnh Nhân " + prefix + " " + i).build())
                        .build());
            }
        }
        log.info(">>> Đã tạo 10 bệnh nhân cho cơ sở: {}", center.getCenterName());
    }
    private void initializeMedicines(MedicineRepository repo) {
        List<Medicine> medicines = List.of(
                // 15 loai thuoc
                Medicine.builder().id("MED-PARA-500").name("Paracetamol").dosage("500mg").unit("Viên").price(new BigDecimal("2000")).build(),
                Medicine.builder().id("MED-AMOX-500").name("Amoxicillin").dosage("500mg").unit("Viên").price(new BigDecimal("5000")).build(),
                Medicine.builder().id("MED-IBU-400").name("Ibuprofen").dosage("400mg").unit("Viên").price(new BigDecimal("3500")).build(),
                Medicine.builder().id("MED-CETY-10").name("Cetirizine").dosage("10mg").unit("Viên").price(new BigDecimal("3000")).build(),
                Medicine.builder().id("MED-METF-850").name("Metformin").dosage("850mg").unit("Viên").price(new BigDecimal("4500")).build(),
                Medicine.builder().id("MED-AMLO-5").name("Amlodipine").dosage("5mg").unit("Viên").price(new BigDecimal("6000")).build(),
                Medicine.builder().id("MED-OMEP-20").name("Omeprazole").dosage("20mg").unit("Viên").price(new BigDecimal("8000")).build(),
                Medicine.builder().id("MED-AUGM-1G").name("Augmentin").dosage("1g").unit("Viên").price(new BigDecimal("15000")).build(),
                Medicine.builder().id("MED-SALB-2").name("Salbutamol").dosage("2mg").unit("Viên").price(new BigDecimal("2500")).build(),
                Medicine.builder().id("MED-GLIC-60").name("Gliclazide").dosage("60mg").unit("Viên").price(new BigDecimal("5500")).build(),
                Medicine.builder().id("MED-ATOR-10").name("Atorvastatin").dosage("10mg").unit("Viên").price(new BigDecimal("12000")).build(),
                Medicine.builder().id("MED-LOSA-50").name("Losartan").dosage("50mg").unit("Viên").price(new BigDecimal("7500")).build(),
                Medicine.builder().id("MED-ESOM-40").name("Esomeprazole").dosage("40mg").unit("Viên").price(new BigDecimal("18000")).build(),
                Medicine.builder().id("MED-CLOP-75").name("Clopidogrel").dosage("75mg").unit("Viên").price(new BigDecimal("22000")).build(),
                Medicine.builder().id("MED-VENT-100").name("Ventolin Evohaler").dosage("100mcg").unit("Bình xịt").price(new BigDecimal("150000")).build()
        );
        repo.saveAll(medicines);
        log.info(">>> Đã khởi tạo 15 loại thuốc mẫu");
    }
    private void initializeTestCategories(TestCategoryRepository repo) {
        List<TestCategory> tests = List.of(
                TestCategory.builder()
                        .id("LAB-BLOOD-01")
                        .name("Tong phan tich te bao mau")
                        .unit("Lan")
                        .price(new BigDecimal("150000"))
                        .description("Kiem tra so luong hong cau, bach cau, tieu cau va cac chi so thieu mau.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-GLU-02")
                        .name("Duong huyet (Glucose)")
                        .unit("Lan")
                        .price(new BigDecimal("50000"))
                        .description("Xet nghiem nong do duong trong mau de sang loc va theo doi benh tieu duong.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-URI-03")
                        .name("Xet nghiem nuoc tieu toan bo")
                        .unit("Lan")
                        .price(new BigDecimal("80000"))
                        .description("Phan tich cac thanh phan hoa hoc trong nuoc tieu, phat hien benh ly than, tiet nieu.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-LIV-04")
                        .name("Chuc nang gan (ALT/AST)")
                        .unit("Lan")
                        .price(new BigDecimal("120000"))
                        .description("Danh gia tinh trang ton thuong gan, men gan cao va cac benh ly ve gan.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-KID-05")
                        .name("Chuc nang than (Ure/Creatinin)")
                        .unit("Lan")
                        .price(new BigDecimal("100000"))
                        .description("Kiem tra kha nang loc cua than va phat hien som suy than.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-LIPID-06")
                        .name("Xet nghiem Mo mau (4 chi so)")
                        .unit("Lan")
                        .price(new BigDecimal("250000"))
                        .description("Kiem tra Cholesterol toan phan, Triglyceride, HDL va LDL de danh gia nguy co tim mach.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-HBA1C-07")
                        .name("Xet nghiem HbA1c")
                        .unit("Lan")
                        .price(new BigDecimal("180000"))
                        .description("Theo doi chi so duong huyet trung binh trong 3 thang gan nhat.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-THY-08")
                        .name("Chuc nang tuyen giap (TSH/T3/T4)")
                        .unit("Lan")
                        .price(new BigDecimal("350000"))
                        .description("Xet nghiem cac hormone de chan doan cuong giap, suy giap.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-HBSAG-09")
                        .name("Viem gan B (HbsAg test nhanh)")
                        .unit("Lan")
                        .price(new BigDecimal("100000"))
                        .description("Xac dinh su hien dien cua virus viem gan B trong co the.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-HCV-10")
                        .name("Viem gan C (HCV Ab test nhanh)")
                        .unit("Lan")
                        .price(new BigDecimal("120000"))
                        .description("Xet nghiem tim khang the virus viem gan C.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-XRAY-11")
                        .name("Chup X-Quang Nguc thang")
                        .unit("Lan")
                        .price(new BigDecimal("150000"))
                        .description("Quan sat hinh anh tim, phoi, suon va cot song nguc.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-ULTRA-12")
                        .name("Sieu am bung tong quat")
                        .unit("Lan")
                        .price(new BigDecimal("200000"))
                        .description("Su dung song sieu am de quan sat cac tang trong o bung nhu gan, mat, lach, than.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-ECG-13")
                        .name("Dien tam do (ECG)")
                        .unit("Lan")
                        .price(new BigDecimal("100000"))
                        .description("Ghi lai hoat dong dien hoc cua tim, phat hien roi loan nhip tim.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-HIV-14")
                        .name("Xet nghiem HIV (Test nhanh)")
                        .unit("Lan")
                        .price(new BigDecimal("100000"))
                        .description("Sang loc khang the HIV de phat hien som nguy co nhiem benh.")
                        .build(),
                TestCategory.builder()
                        .id("LAB-STOOL-15")
                        .name("Soi phan tim ky sinh trung")
                        .unit("Lan")
                        .price(new BigDecimal("70000"))
                        .description("Kiem tra trung va ky sinh trung duong ruot trong mau phan.")
                        .build()
        );
        repo.saveAll(tests);
        log.info(">>> Đã khởi tạo 15 loại xét nghiệm mẫu kèm mô tả đầy đủ");
    }

    private record DoctorData(String email, String phone, String fullName, String specialty, Gender gender, String certificate) {}
}