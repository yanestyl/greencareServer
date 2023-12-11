package com.yanestyl.greencare.services;

import com.yanestyl.greencare.dto.JwtAuthenticationResponse;
import com.yanestyl.greencare.dto.RefreshTokenRequest;
import com.yanestyl.greencare.dto.SignInRequest;
import com.yanestyl.greencare.dto.SignUpRequest;

public interface AuthenticationService {

    void signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SignInRequest signInRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
