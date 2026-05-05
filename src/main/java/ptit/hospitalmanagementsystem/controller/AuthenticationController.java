package ptit.hospitalmanagementsystem.controller;


import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptit.hospitalmanagementsystem.dto.ApiResponse;
import ptit.hospitalmanagementsystem.dto.request.AuthenticationRequest;
import ptit.hospitalmanagementsystem.dto.request.IntrospectRequest;
import ptit.hospitalmanagementsystem.dto.request.LogoutRequest;
import ptit.hospitalmanagementsystem.dto.request.RefreshRequest;
import ptit.hospitalmanagementsystem.dto.respond.AuthenticationResponse;
import ptit.hospitalmanagementsystem.dto.respond.IntrospectResponse;
import ptit.hospitalmanagementsystem.service.AuthenticationService;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    //login
    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
    //verify token
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {

        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
    //logout
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {

        authenticationService.logout(request);
        return ApiResponse.<Void>builder().message("Logout successfully!")
                .build();
    }
    //refesh token
    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refesh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
}
