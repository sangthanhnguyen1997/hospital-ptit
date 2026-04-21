package ptit.hospitalmanagementsystem.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    List<T> items;      // Danh sách Patient hoặc Facility
    long totalElements;
    int totalPages;
    int page;
    int size;
}