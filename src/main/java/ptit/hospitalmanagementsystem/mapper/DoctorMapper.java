package ptit.hospitalmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptit.hospitalmanagementsystem.dto.request.DoctorCreationRequest;
import ptit.hospitalmanagementsystem.entity.Doctor;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "healthcareCenter", ignore = true)
    Doctor toUser(DoctorCreationRequest request);

}
