package com.yanestyl.greencare.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {

    private String phoneNumber;
    private String password;
}
