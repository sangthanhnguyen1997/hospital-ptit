package ptit.hospitalmanagementsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.hospitalmanagementsystem.dto.request.UserCreationRequest;
import ptit.hospitalmanagementsystem.dto.request.UserUpdateRequest;
import ptit.hospitalmanagementsystem.dto.respond.UserResponse;
import ptit.hospitalmanagementsystem.entity.User;
import ptit.hospitalmanagementsystem.enums.Role;
import ptit.hospitalmanagementsystem.exception.AppException;
import ptit.hospitalmanagementsystem.exception.ErrorCode;
import ptit.hospitalmanagementsystem.mapper.UserMapper;
import ptit.hospitalmanagementsystem.repository.UserRepository;



@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);


        user.setPassword(passwordEncoder.encode(request.getPassword()));


        // Save xong phải Map sang UserResponse rồi mới trả về
        return userMapper.toUserResponse(userRepository.save(user));
    }


    public UserResponse getUerById(String id) {
        log.info("In method get user by id: {}", id);
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!")));
    }


    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        userMapper.updateUser(user, request);

        // Xử lý password nếu có update
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // 3. Xử lý Roles: Lấy danh sách entity từ DB dựa trên list ID trong request

        user.setRoles(Role.valueOf(request.getRole()));

        return userMapper.toUserResponse(userRepository.save(user));
    }
    //sau khi dang nhap
    public UserResponse getMyInfo(){
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
