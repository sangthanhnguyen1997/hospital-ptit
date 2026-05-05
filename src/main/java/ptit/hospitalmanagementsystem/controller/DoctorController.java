package ptit.hospitalmanagementsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ptit.hospitalmanagementsystem.dto.ApiResponse;
import ptit.hospitalmanagementsystem.dto.PageResponse;
import ptit.hospitalmanagementsystem.dto.request.DoctorRequest;
import ptit.hospitalmanagementsystem.dto.respond.DoctorResponse;
import ptit.hospitalmanagementsystem.service.DoctorService;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorController {
    DoctorService doctorService;

    @PostMapping
    ApiResponse<DoctorResponse> create(@RequestBody DoctorRequest request) {
        return ApiResponse.<DoctorResponse>builder()
                .code(1000)
                .result(doctorService.createDoctor(request))
                .build();
    }

    /**
     * Endpoint lấy danh sách bác sĩ có phân trang
     * URL ví dụ: GET /doctors?page=1&size=10
     */
    @GetMapping
    ApiResponse<PageResponse<DoctorResponse>> getPaging(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<DoctorResponse>>builder()
                .code(1000)
                .result(doctorService.getDoctorsPaging(page, size))
                .build();
    }

    /**
     * Endpoint lấy toàn bộ danh sách bác sĩ (không phân trang)
     */
    @GetMapping("/all")
    ApiResponse<List<DoctorResponse>> getAll() {
        return ApiResponse.<List<DoctorResponse>>builder()
                .code(1000)
                .result(doctorService.getAllDoctors())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<DoctorResponse> getById(@PathVariable Long id) {
        return ApiResponse.<DoctorResponse>builder()
                .code(1000)
                .result(doctorService.getDoctorById(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<DoctorResponse> update(@PathVariable Long id, @RequestBody DoctorRequest request) {
        return ApiResponse.<DoctorResponse>builder()
                .code(1000)
                .result(doctorService.updateDoctor(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> delete(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Doctor has been deleted successfully")
                .build();
    }
}