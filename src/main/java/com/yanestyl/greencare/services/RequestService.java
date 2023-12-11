package com.yanestyl.greencare.services;

import com.yanestyl.greencare.dto.CreateRequestInfo;
import com.yanestyl.greencare.dto.OneRequestDTO;
import com.yanestyl.greencare.dto.RequestsListDTO;
import com.yanestyl.greencare.entity.Request;

import java.util.List;

public interface RequestService {

    List<RequestsListDTO> getUserRequestsList(String jwtToken);

    OneRequestDTO getOneUserRequest(Long requestId);

    Request createRequest(CreateRequestInfo createRequestInfo, String jwtToken);

}
