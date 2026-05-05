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
import ptit.hospitalmanagementsystem.dto.request.HealthcareCenterRequest;
import ptit.hospitalmanagementsystem.dto.respond.HealthcareCenterResponse;
import ptit.hospitalmanagementsystem.entity.HealthcareCenter;
import ptit.hospitalmanagementsystem.exception.AppException;
import ptit.hospitalmanagementsystem.exception.ErrorCode;
import ptit.hospitalmanagementsystem.mapper.HealthcareCenterMapper;
import ptit.hospitalmanagementsystem.repository.HealthcareCenterRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HealthcareService {
    HealthcareCenterRepository healthcareCenterRepository;
    HealthcareCenterMapper healthcareCenterMapper;

    public List<HealthcareCenterResponse> getHealthcareCenters() {


        return healthcareCenterRepository.findAll().stream()
                .map(healthcareCenterMapper::toResponse)
                .toList();
    }

    public HealthcareCenterResponse addHealthcare(HealthcareCenterRequest request){
        //nếu trùng tên thì ném ngoại lệ
        if(healthcareCenterRepository.existsByCenterName(request.getCenterName()))
            throw new AppException(ErrorCode.HEALTHCARE_ERROR);

        //mapping HealthcareCenterrequest -> healthcare center
        HealthcareCenter newCenter = healthcareCenterMapper.toHealthcareCenter(request);
        //lưu lại
        healthcareCenterRepository.save(newCenter);
        //mapping và trả về trung tâm mơi
        return healthcareCenterMapper.toResponse(newCenter);
    }
    public HealthcareCenterResponse updateHealthcare(HealthcareCenterRequest request, Long id){
        //lay healthcareCenter theo id
        HealthcareCenter healthcareCenter = healthcareCenterRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.HEALTHCARE_ERROR));
        //map thong tin update vao healthCareCenter
        healthcareCenterMapper.updateHealthcareCenter(healthcareCenter, request);
        //luu lai
        healthcareCenterRepository.save(healthcareCenter);
        //tra ve thong tin moi
        return healthcareCenterMapper.toResponse(healthcareCenter);
    }

    public void deleteHealthcare(Long healthcareCenterID){
        healthcareCenterRepository.deleteById(healthcareCenterID);
    }
    public HealthcareCenterResponse getHealthcareById(Long healthcareCenterID){
        HealthcareCenter healthcareCenter= healthcareCenterRepository.findById(healthcareCenterID).orElseThrow(() -> new AppException(ErrorCode.HEALTHCARE_ERROR));
        return  healthcareCenterMapper.toResponse(healthcareCenter);

    }
    //trả về theo trang
    public PageResponse<HealthcareCenterResponse> getHealthcareCenters(int page, int size) {
        // 1. Tạo đối tượng Pageable (có thể thêm Sort ở đây)
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        // 2. Gọi Repository trả về Page thay vì List
        Page<HealthcareCenter> pageData = healthcareCenterRepository.findAll(pageable);

        // 3. Map sang DTO
        List<HealthcareCenterResponse> list = pageData.getContent().stream()
                .map(healthcareCenterMapper::toResponse)
                .toList();

        // 4. Trả về đối tượng bọc ngoài chứa thông tin phân trang
        return PageResponse.<HealthcareCenterResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .pageSize(pageData.getSize())
                .data(list)
                .build();
    }
}
