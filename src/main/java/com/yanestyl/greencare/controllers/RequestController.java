package com.yanestyl.greencare.controllers;

import com.yanestyl.greencare.dto.CreateRequestInfo;
import com.yanestyl.greencare.dto.OneRequestDTO;
import com.yanestyl.greencare.dto.RequestsListDTO;
import com.yanestyl.greencare.entity.Photo;
import com.yanestyl.greencare.entity.Request;
import com.yanestyl.greencare.entity.User;
import com.yanestyl.greencare.repository.UserRepository;
import com.yanestyl.greencare.services.JWTService;
import com.yanestyl.greencare.services.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/requests")
@AllArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    @GetMapping("/getOneUserRequest/{requestId}")
    public ResponseEntity<OneRequestDTO> getOneUserRequest(@PathVariable Long requestId) {
        OneRequestDTO oneRequestDTO = requestService.getOneUserRequest(requestId);

        if (oneRequestDTO != null) {
            return ResponseEntity.ok(oneRequestDTO);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getUserRequests")
    public ResponseEntity<List<RequestsListDTO>> getUserRequests(@RequestHeader("Authorization") String authorizationHeader) {
        // Извлекаем jwt с учетом префикса
        String jwtToken = authorizationHeader.substring(7);

        List<RequestsListDTO> requestsListDTOList = requestService.getUserRequestsList(jwtToken);

        if (requestsListDTOList != null) {
            return ResponseEntity.ok(requestsListDTOList);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createRequest(
            @RequestBody CreateRequestInfo createRequestInfo,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Извлекаем jwt с учетом префикса
        String jwtToken = authorizationHeader.substring(7);

        Request newRequest = requestService.createRequest(createRequestInfo, jwtToken);

        if (newRequest != null) {
            return ResponseEntity.ok(newRequest.getId());
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}