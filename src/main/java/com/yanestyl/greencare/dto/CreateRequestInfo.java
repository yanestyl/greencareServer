package com.yanestyl.greencare.dto;

import lombok.Data;

@Data
public class CreateRequestInfo {
    private String description;
    private double lng;
    private double lat;
}
