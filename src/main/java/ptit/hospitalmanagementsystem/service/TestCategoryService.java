package ptit.hospitalmanagementsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ptit.hospitalmanagementsystem.dto.PageResponse;
import ptit.hospitalmanagementsystem.dto.respond.TestCategoryResponse;
import ptit.hospitalmanagementsystem.mapper.TestCategoryMapper;
import ptit.hospitalmanagementsystem.repository.TestCategoryRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestCategoryService {
    TestCategoryRepository testCategoryRepository;
    TestCategoryMapper testCategoryMapper;

    public PageResponse<TestCategoryResponse> getTestsPaging(String name, int page, int size) {
        // Sắp xếp theo tên A-Z để bác sĩ dễ tìm
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name").ascending());

        var pageData = (name != null && !name.isEmpty())
                ? testCategoryRepository.findAllByNameContainingIgnoreCase(name, pageable)
                : testCategoryRepository.findAll(pageable);

        return PageResponse.<TestCategoryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(testCategoryMapper::toResponse)
                        .toList())
                .build();
    }
}