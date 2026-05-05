package ptit.hospitalmanagementsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ptit.hospitalmanagementsystem.dto.ApiResponse;
import ptit.hospitalmanagementsystem.dto.PageResponse;
import ptit.hospitalmanagementsystem.dto.request.PatientRequest;
import ptit.hospitalmanagementsystem.dto.respond.PatientResponse;
import ptit.hospitalmanagementsystem.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientController {
    PatientService patientService;

    @PostMapping("/create")
    ApiResponse<PatientResponse> create(@RequestBody PatientRequest request) {
        return ApiResponse.<PatientResponse>builder()
                .code(1000)
                .result(patientService.createPatient(request))
                .build();
    }

    @GetMapping
    ApiResponse<PageResponse<PatientResponse>> getPaging(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<PatientResponse>>builder()
                .code(1000)
                .result(patientService.getPatientsPaging(page, size))
                .build();
    }

    @GetMapping("/all")
    ApiResponse<List<PatientResponse>> getAll() {
        return ApiResponse.<List<PatientResponse>>builder()
                .code(1000)
                .result(patientService.getAllPatients())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<PatientResponse> getById(@PathVariable Long id) {
        return ApiResponse.<PatientResponse>builder()
                .code(1000)
                .result(patientService.getPatientById(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<PatientResponse> update(@PathVariable Long id, @RequestBody PatientRequest request) {
        return ApiResponse.<PatientResponse>builder()
                .code(1000)
                .result(patientService.updatePatient(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> delete(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Xóa bệnh nhân thành công")
                .build();
    }
}