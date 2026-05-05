package ptit.hospitalmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ptit.hospitalmanagementsystem.dto.request.HealthcareCenterRequest;
import ptit.hospitalmanagementsystem.dto.respond.HealthcareCenterResponse;
import ptit.hospitalmanagementsystem.entity.HealthcareCenter;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HealthcareCenterMapper {
    HealthcareCenterResponse toResponse(HealthcareCenter healthcareCenter);

    // Thử đổi tên tham số ngắn gọn là 'request' cho đồng bộ
    HealthcareCenter toHealthcareCenter(HealthcareCenterRequest request);

    void updateHealthcareCenter(@MappingTarget HealthcareCenter healthcareCenter, HealthcareCenterRequest request);
}

