package ptit.hospitalmanagementsystem.controller;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ptit.hospitalmanagementsystem.dto.ApiResponse;
import ptit.hospitalmanagementsystem.dto.PageResponse;
import ptit.hospitalmanagementsystem.dto.respond.TestCategoryResponse;
import ptit.hospitalmanagementsystem.service.TestCategoryService;

@RestController
@RequestMapping("/test-categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestCategoryController {
    TestCategoryService testCategoryService;

    @GetMapping
    ApiResponse<PageResponse<TestCategoryResponse>> getPaging(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<TestCategoryResponse>>builder()
                .code(1000)
                .result(testCategoryService.getTestsPaging(name, page, size))
                .build();
    }
}