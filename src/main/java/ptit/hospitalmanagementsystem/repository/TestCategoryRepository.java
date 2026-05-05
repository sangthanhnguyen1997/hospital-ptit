package ptit.hospitalmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.hospitalmanagementsystem.entity.TestCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestCategoryRepository extends JpaRepository<TestCategory, String> {
    // Tìm theo tên xét nghiệm có chứa từ khóa
    Page<TestCategory> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}