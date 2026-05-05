package ptit.hospitalmanagementsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ptit.hospitalmanagementsystem.dto.PageResponse;
import ptit.hospitalmanagementsystem.dto.respond.ExaminationRoomResponse;
import ptit.hospitalmanagementsystem.entity.ExaminationRoom;
import ptit.hospitalmanagementsystem.mapper.ExaminationRoomMapper;
import ptit.hospitalmanagementsystem.repository.ExaminationRoomRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExaminationRoomService {
    ExaminationRoomRepository roomRepository;
    ExaminationRoomMapper roomMapper;

    public PageResponse<ExaminationRoomResponse> getRoomsPaging(String centerName, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());

        Page<ExaminationRoom> pageData;

        // Nếu centerName không rỗng thì lọc theo tên cơ sở
        if (centerName != null && !centerName.isEmpty()) {
            pageData = roomRepository.findAllByHealthcareCenterCenterNameContaining(centerName, pageable);
        } else {
            pageData = roomRepository.findAll(pageable);
        }

        return PageResponse.<ExaminationRoomResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(roomMapper::toResponse)
                        .toList())
                .build();
    }
}