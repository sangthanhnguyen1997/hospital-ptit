package ptit.hospitalmanagementsystem.mapper;
import org.mapstruct.Mapper;
import ptit.hospitalmanagementsystem.dto.respond.TestCategoryResponse;
import ptit.hospitalmanagementsystem.entity.TestCategory;

@Mapper(componentModel = "spring")
public interface TestCategoryMapper {
    // Tự động map các trường cùng tên: id, name, unit, price, description, isActive
    TestCategoryResponse toResponse(TestCategory entity);
}