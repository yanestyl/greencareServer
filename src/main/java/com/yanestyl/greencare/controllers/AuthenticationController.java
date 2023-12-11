package com.yanestyl.greencare.controllers;

import com.yanestyl.greencare.dto.JwtAuthenticationResponse;
import com.yanestyl.greencare.dto.RefreshTokenRequest;
import com.yanestyl.greencare.dto.SignInRequest;
import com.yanestyl.greencare.dto.SignUpRequest;
import com.yanestyl.greencare.entity.User;
import com.yanestyl.greencare.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignUpRequest signUpRequest) {
        try {
            authenticationService.signup(signUpRequest);

            SignInRequest signInRequest = SignInRequest.builder()
                    .phoneNumber(signUpRequest.getPhoneNumber())
                    .password(signUpRequest.getPassword())
                    .build();

            return ResponseEntity.ok(authenticationService.signin(signInRequest));
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Пользователь с указанными данными уже зарегистрирован.";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@RequestBody SignInRequest signInRequest) {
        try {
            JwtAuthenticationResponse response = authenticationService.signin(signInRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            String errorMessage = "Неправильные учетные данные. Пожалуйста, проверьте ваш логин и пароль.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

}
