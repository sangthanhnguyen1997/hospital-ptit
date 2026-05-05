package ptit.hospitalmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptit.hospitalmanagementsystem.dto.respond.ExaminationRoomResponse;
import ptit.hospitalmanagementsystem.entity.ExaminationRoom;

@Mapper(componentModel = "spring")
public interface ExaminationRoomMapper {
    @Mapping(target = "healthcareCenterId", source = "healthcareCenter.id")
    @Mapping(target = "centerName", source = "healthcareCenter.centerName")
    ExaminationRoomResponse toResponse(ExaminationRoom entity);
}