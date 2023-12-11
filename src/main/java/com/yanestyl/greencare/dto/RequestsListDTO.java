package com.yanestyl.greencare.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestsListDTO {
    private Long request_id;
    private String description;
    private Long photo_id;
    private int status;

}
