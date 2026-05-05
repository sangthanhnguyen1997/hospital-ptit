package ptit.hospitalmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ptit.hospitalmanagementsystem.dto.request.DoctorRequest;
import ptit.hospitalmanagementsystem.dto.respond.DoctorResponse;
import ptit.hospitalmanagementsystem.entity.Doctor;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "healthcareCenter", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "user", ignore = true)
    Doctor toDoctor(DoctorRequest request);

    // Map từ healthcareCenter.centerName sang healthcareCenterName trong Response
    @Mapping(target = "healthcareCenterName", source = "healthcareCenter.centerName")
    @Mapping(target = "gender", source = "gender")
    DoctorResponse toDoctorResponse(Doctor doctor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "healthcareCenter", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateDoctor(@MappingTarget Doctor doctor, DoctorRequest request);
}
