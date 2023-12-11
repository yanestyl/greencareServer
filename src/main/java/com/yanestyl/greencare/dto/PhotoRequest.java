package com.yanestyl.greencare.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoRequest {
    private Long request_id;
    private boolean isMain;
}
