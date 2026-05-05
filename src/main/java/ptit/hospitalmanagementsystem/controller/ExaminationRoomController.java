package ptit.hospitalmanagementsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ptit.hospitalmanagementsystem.dto.ApiResponse;
import ptit.hospitalmanagementsystem.dto.PageResponse;
import ptit.hospitalmanagementsystem.dto.respond.ExaminationRoomResponse;
import ptit.hospitalmanagementsystem.service.ExaminationRoomService;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExaminationRoomController {
    ExaminationRoomService roomService;

    @GetMapping
    ApiResponse<PageResponse<ExaminationRoomResponse>> getPaging(
            @RequestParam(value = "centerName", required = false) String centerName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ExaminationRoomResponse>>builder()
                .code(1000)
                .result(roomService.getRoomsPaging(centerName, page, size))
                .build();
    }
}