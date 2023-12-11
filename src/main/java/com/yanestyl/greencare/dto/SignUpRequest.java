package com.yanestyl.greencare.dto;


import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private String phoneNumber;
    private String password;
}
