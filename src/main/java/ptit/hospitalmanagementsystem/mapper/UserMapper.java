package ptit.hospitalmanagementsystem.mapper;


import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ptit.hospitalmanagementsystem.dto.request.UserCreationRequest;
import ptit.hospitalmanagementsystem.dto.request.UserUpdateRequest;
import ptit.hospitalmanagementsystem.dto.respond.UserResponse;
import ptit.hospitalmanagementsystem.entity.User;

@Mapper(componentModel = "spring")//khai bao dung mapper trong spring
public interface UserMapper {


    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);


    @Mapping(target="roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);



}
