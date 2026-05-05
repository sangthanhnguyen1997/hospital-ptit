package ptit.hospitalmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ptit.hospitalmanagementsystem.dto.request.PatientRequest;
import ptit.hospitalmanagementsystem.dto.respond.PatientResponse;
import ptit.hospitalmanagementsystem.entity.Patient;
@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "examinationTickets", ignore = true)
    Patient toPatient(PatientRequest request);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "createdAt", source = "createdDate")
        // Target 'id' sẽ tự động lấy từ Source 'id' của Patient entity
    PatientResponse toPatientResponse(Patient patient);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true) // Luôn ignore ID khi update để tránh lỗi thay đổi PK
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "examinationTickets", ignore = true)
    void updatePatient(@MappingTarget Patient patient, PatientRequest request);
}