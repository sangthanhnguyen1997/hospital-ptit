package ptit.hospitalmanagementsystem.mapper;

import org.mapstruct.Mapper;
import ptit.hospitalmanagementsystem.dto.request.DoctorCreationRequest;
import ptit.hospitalmanagementsystem.entity.Doctor;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    Doctor toUser(DoctorCreationRequest request);

}
