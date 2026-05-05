package ptit.hospitalmanagementsystem.controller;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ptit.hospitalmanagementsystem.dto.ApiResponse;
import ptit.hospitalmanagementsystem.dto.PageResponse;
import ptit.hospitalmanagementsystem.dto.request.*;
import ptit.hospitalmanagementsystem.dto.respond.AuthenticationResponse;
import ptit.hospitalmanagementsystem.dto.respond.HealthcareCenterResponse;
import ptit.hospitalmanagementsystem.dto.respond.IntrospectResponse;
import ptit.hospitalmanagementsystem.service.HealthcareService;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/healthcare")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HealthcareController {
    HealthcareService healthcareService;
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<HealthcareCenterResponse>> getHealthcare(){
        //xu ly service
        List<HealthcareCenterResponse> response = healthcareService.getHealthcareCenters();

        return ApiResponse.<List<HealthcareCenterResponse>>builder()
                .result(response)
                .build();
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<HealthcareCenterResponse>> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<HealthcareCenterResponse>>builder()
                .result(healthcareService.getHealthcareCenters(page, size))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<HealthcareCenterResponse> getHealthcareById(@PathVariable Long id){

        return ApiResponse.<HealthcareCenterResponse>builder()
                .result(healthcareService.getHealthcareById(id))
                .build();
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<HealthcareCenterResponse> createHealthcare(@RequestBody HealthcareCenterRequest request){
        //xu ly service
        HealthcareCenterResponse response = healthcareService.addHealthcare(request);

        return ApiResponse.<HealthcareCenterResponse>builder()
                .result(response)
                .build();
    }

    @PostMapping("/{healthcareId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<HealthcareCenterResponse> updateHealthcare(@RequestBody HealthcareCenterRequest request, @PathVariable("healthcareId") Long id){
        //xu ly service
        HealthcareCenterResponse response = healthcareService.updateHealthcare(request, id);

        return ApiResponse.<HealthcareCenterResponse>builder()
                .result(response)
                .build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteHealthcare(@PathVariable Long id) {
        healthcareService.deleteHealthcare(id);
        return ApiResponse.<Void>builder().build();
    }


}
