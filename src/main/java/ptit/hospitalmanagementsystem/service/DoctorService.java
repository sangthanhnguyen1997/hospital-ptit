package ptit.hospitalmanagementsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptit.hospitalmanagementsystem.dto.PageResponse;
import ptit.hospitalmanagementsystem.dto.request.DoctorRequest;
import ptit.hospitalmanagementsystem.dto.respond.DoctorResponse;
import ptit.hospitalmanagementsystem.entity.Doctor;
import ptit.hospitalmanagementsystem.entity.HealthcareCenter;
import ptit.hospitalmanagementsystem.entity.User;
import ptit.hospitalmanagementsystem.enums.Gender;
import ptit.hospitalmanagementsystem.enums.Role;
import ptit.hospitalmanagementsystem.mapper.DoctorMapper;
import ptit.hospitalmanagementsystem.repository.DoctorRepository;
import ptit.hospitalmanagementsystem.repository.HealthcareCenterRepository;
import ptit.hospitalmanagementsystem.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorService {
    DoctorRepository doctorRepository;
    UserRepository userRepository;
    HealthcareCenterRepository healthcareCenterRepository;
    DoctorMapper doctorMapper;
    PasswordEncoder passwordEncoder;

    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        // 1. Kiểm tra username tồn tại (Sử dụng email làm username)
        if (userRepository.existsByUsername(request.getEmail())) {
            throw new RuntimeException("Email/Username already exists: " + request.getEmail());
        }

        // 2. Kiểm tra và lấy cơ sở y tế
        HealthcareCenter center = healthcareCenterRepository.findById(request.getHealthcareCenterId())
                .orElseThrow(() -> new RuntimeException("Healthcare Center not found with ID: " + request.getHealthcareCenterId()));

        // 3. Mapping Request sang Entity
        Doctor doctor = doctorMapper.toDoctor(request);
        doctor.setHealthcareCenter(center);
        doctor.setGender(mapStringToGender(request.getGender()));

        // 4. Tạo tài khoản User tương ứng
        // Username = Email, Password mặc định = Số điện thoại
        User user = User.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPhoneNumber()))
                .fullName(request.getFullName())
                .roles(Role.DOCTOR)
                .build();

        // 5. Thiết lập quan hệ 2 chiều giữa Doctor và User thông qua Helper Method
        doctor.setUser(user);

        // 6. Lưu vào database và trả về kết quả
        return doctorMapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    public PageResponse<DoctorResponse> getDoctorsPaging(int page, int size) {
        // Sắp xếp theo ID giảm dần để bác sĩ mới tạo hiện lên đầu
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        var pageData = doctorRepository.findAll(pageable);

        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(doctorMapper::toDoctorResponse)
                        .toList())
                .build();
    }
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDoctorResponse)
                .toList();
    }

    public DoctorResponse getDoctorById(Long id) {
        return doctorMapper.toDoctorResponse(
                doctorRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id))
        );
    }

    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        // Update thông tin cơ bản qua Mapper
        doctorMapper.updateDoctor(doctor, request);

        // Cập nhật Gender nếu có
        if (request.getGender() != null) {
            doctor.setGender(mapStringToGender(request.getGender()));
        }

        // Cập nhật lại cơ sở y tế nếu có thay đổi ID
        if (request.getHealthcareCenterId() != null) {
            HealthcareCenter center = healthcareCenterRepository.findById(request.getHealthcareCenterId())
                    .orElseThrow(() -> new RuntimeException("Healthcare Center not found"));
            doctor.setHealthcareCenter(center);
        }

        // Đồng bộ lại FullName cho User nếu cần
        if (doctor.getUser() != null) {
            doctor.getUser().setFullName(request.getFullName());
        }

        return doctorMapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found with ID: " + id);
        }
        doctorRepository.deleteById(id);
    }

    /**
     * Helper method to map String to Gender Enum
     */
    private Gender mapStringToGender(String genderStr) {
        if (genderStr == null) return Gender.OTHER;
        try {
            return Gender.valueOf(genderStr.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return Gender.OTHER; // Trả về OTHER nếu input không khớp (ví dụ: "nam", "nu"...)
        }
    }
}