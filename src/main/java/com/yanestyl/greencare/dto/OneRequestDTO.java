package com.yanestyl.greencare.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OneRequestDTO {
    private int status;
    private List<Long> photoIdList;
    private String description;
    private double lng;
    private double lat;
}
