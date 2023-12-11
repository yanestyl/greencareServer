package com.yanestyl.greencare.services.impl;

import com.yanestyl.greencare.dto.JwtAuthenticationResponse;
import com.yanestyl.greencare.dto.RefreshTokenRequest;
import com.yanestyl.greencare.dto.SignInRequest;
import com.yanestyl.greencare.dto.SignUpRequest;
import com.yanestyl.greencare.entity.User;
import com.yanestyl.greencare.repository.UserRepository;
import com.yanestyl.greencare.services.AuthenticationService;
import com.yanestyl.greencare.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public void signup(SignUpRequest signUpRequest) {
        User user = User.builder()
                .phoneNumber(signUpRequest.getPhoneNumber())
                .name(signUpRequest.getName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();

        userRepository.save(user);
    }

    public JwtAuthenticationResponse signin(SignInRequest signInRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getPhoneNumber(),
                signInRequest.getPassword()));

        var user = userRepository.findByPhoneNumber(signInRequest.getPhoneNumber()).orElseThrow(() -> new IllegalArgumentException("Invalid phoneNumber or password."));

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userPhoneNumber = jwtService.extractUserName(refreshTokenRequest.getToken());

        User user = userRepository.findByPhoneNumber(userPhoneNumber).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;

        }
        return null;
    }

}
