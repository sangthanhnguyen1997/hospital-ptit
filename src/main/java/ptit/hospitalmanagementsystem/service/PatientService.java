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
import ptit.hospitalmanagementsystem.dto.request.PatientRequest;
import ptit.hospitalmanagementsystem.dto.respond.PatientResponse;
import ptit.hospitalmanagementsystem.entity.Patient;
import ptit.hospitalmanagementsystem.entity.User;
import ptit.hospitalmanagementsystem.enums.Gender;
import ptit.hospitalmanagementsystem.enums.Role;
import ptit.hospitalmanagementsystem.mapper.PatientMapper;
import ptit.hospitalmanagementsystem.repository.PatientRepository;
import ptit.hospitalmanagementsystem.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientService {
    PatientRepository patientRepository;
    UserRepository userRepository;
    PatientMapper patientMapper;
    PasswordEncoder passwordEncoder;
    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        if (userRepository.existsByUsername(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number already registered!");
        }

        Patient patient = patientMapper.toPatient(request);
        patient.setGender(mapStringToGender(request.getGender()));

        User user = User.builder()
                .username(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPhoneNumber()))
                .fullName(request.getFullName())
                .roles(Role.USER)
                .build();

        patient.setUser(user);

        // Sau khi save, đối tượng 'savedPatient' sẽ có ID sinh ra từ Database
        Patient savedPatient = patientRepository.save(patient);

        return patientMapper.toPatientResponse(savedPatient);
    }

    public PageResponse<PatientResponse> getPatientsPaging(int page, int size) {
        // Updated sort field to match Entity's createdDate
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
        var pageData = patientRepository.findAll(pageable);

        return PageResponse.<PatientResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(patientMapper::toPatientResponse)
                        .toList())
                .build();
    }

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toPatientResponse)
                .toList();
    }

    public PatientResponse getPatientById(Long id) {
        return patientMapper.toPatientResponse(
                patientRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Patient not found"))
        );
    }
    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Mapper sẽ copy dữ liệu từ request sang patient hiện có (trừ ID)
        patientMapper.updatePatient(patient, request);

        if (request.getGender() != null) {
            patient.setGender(mapStringToGender(request.getGender()));
        }

        // Save lại để cập nhật DB
        Patient updatedPatient = patientRepository.save(patient);

        return patientMapper.toPatientResponse(updatedPatient);
    }

    @Transactional
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    private Gender mapStringToGender(String genderStr) {
        if (genderStr == null) return Gender.OTHER;
        try {
            return Gender.valueOf(genderStr.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return Gender.OTHER; // Default to OTHER if input is invalid
        }
    }
}