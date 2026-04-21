package ptit.hospitalmanagementsystem.controller;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ptit.hospitalmanagementsystem.dto.request.UserCreationRequest;
import ptit.hospitalmanagementsystem.dto.request.UserUpdateRequest;
import ptit.hospitalmanagementsystem.dto.respond.UserResponse;
import ptit.hospitalmanagementsystem.mapper.UserMapper;
import ptit.hospitalmanagementsystem.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping
    UserResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        return userService.createUser(request);

    }


    @GetMapping("/myInfo")
    UserResponse getMyInfo() {
        return userService.getMyInfo();

    }
    @GetMapping("/{userId}")
   UserResponse getUser(@PathVariable("userId") String userId) {
        return userService.getUerById(userId);

    }

    @PutMapping("/{userId}")
 UserResponse updateUser(@RequestBody UserUpdateRequest request, @PathVariable("userId") String userId) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return "User has been deleted.";

    }
}
