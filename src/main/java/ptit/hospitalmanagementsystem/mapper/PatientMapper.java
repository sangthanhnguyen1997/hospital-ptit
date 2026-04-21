package ptit.hospitalmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ptit.hospitalmanagementsystem.dto.request.PatientCreationRequest;
import ptit.hospitalmanagementsystem.dto.request.UserUpdateRequest;
import ptit.hospitalmanagementsystem.dto.respond.PatientCreationResponse;
import ptit.hospitalmanagementsystem.dto.respond.UserResponse;
import ptit.hospitalmanagementsystem.entity.Patient;
import ptit.hospitalmanagementsystem.entity.User;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientCreationResponse toPatient(PatientCreationRequest patientCreationRequest);


    @Mapping(target="roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);


    UserResponse toUserResponse(User user);
}
