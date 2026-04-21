package ptit.hospitalmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder//tranh nham lanh builder cua lombok va mapstruct nhe
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)//khong dua cac truong null vao object api
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse <T>{
    int code;
    String message;
    T result;//dung generic trong java


}
