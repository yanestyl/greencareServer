package com.yanestyl.greencare.services.impl;

import com.yanestyl.greencare.dto.CreateRequestInfo;
import com.yanestyl.greencare.dto.OneRequestDTO;
import com.yanestyl.greencare.dto.RequestsListDTO;
import com.yanestyl.greencare.entity.LocationIssue;
import com.yanestyl.greencare.entity.Photo;
import com.yanestyl.greencare.entity.Request;
import com.yanestyl.greencare.entity.User;
import com.yanestyl.greencare.repository.PhotoRepository;
import com.yanestyl.greencare.repository.RequestRepository;
import com.yanestyl.greencare.repository.UserRepository;
import com.yanestyl.greencare.services.JWTService;
import com.yanestyl.greencare.services.LocationIssueService;
import com.yanestyl.greencare.services.PhotoService;
import com.yanestyl.greencare.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final LocationIssueService locationIssueService;
    private final RequestRepository requestRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final PhotoService photoService;
    private final JWTService jwtService;

    @Override
    public OneRequestDTO getOneUserRequest(Long requestId) {
        Optional<Request> requestOptional = requestRepository.findById(requestId);

        OneRequestDTO oneRequestDTO = null;

        if (requestOptional.isPresent()) {
            Request request = requestOptional.get();
            List<Photo> photoList = photoRepository.findByRequestId(requestId);
            List<Long> photoIdList = photoList.stream()
                    .map(Photo::getId)
                    .toList();

            oneRequestDTO = OneRequestDTO.builder()
                    .status(request.getLocationIssue().getStatus())
                    .photoIdList(photoIdList)
                    .description(request.getDescription())
                    .lng(request.getLocationIssue().getLng())
                    .lat(request.getLocationIssue().getLat())
                    .build();
        }

        return oneRequestDTO;
    }

    @Override
    public Request createRequest(CreateRequestInfo createRequestInfo, String jwtToken) {
        // получаем юзера по номеру из jwt
        String userPhoneNumber = jwtService.extractUserName(jwtToken);
        Optional<User> userOptional = userRepository.findByPhoneNumber(userPhoneNumber);

        Request newRequest = null;

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // получаем или создаем проблемное место
            // Приблизительное расстояние в градусах
            double degreesFor100Meters = 200 / 111000.0;
            LocationIssue locationIssue = locationIssueService.getLocationIssueOrCreateIfNotExists(createRequestInfo.getLng(), createRequestInfo.getLat(), degreesFor100Meters);

            // Создаем новую заявку
             newRequest = Request.builder()
                    .user(user)
                    .description(createRequestInfo.getDescription())
                    .locationIssue(locationIssue)
                    .build();

            requestRepository.save(newRequest);
        }

        return newRequest;

    }

    @Override
    public List<RequestsListDTO> getUserRequestsList(String jwtToken) {
        // получаем юзера по номеру из jwt
        String userPhoneNumber = jwtService.extractUserName(jwtToken);
        Optional<User> userOptional = userRepository.findByPhoneNumber(userPhoneNumber);

        List<RequestsListDTO> requestsListDTOList = null;

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            System.out.println(user);

            List<Request> userRequests = requestRepository.findByUserId(user.getId());

            System.out.println(userRequests);

            requestsListDTOList = userRequests.stream()
                    .map(request -> {
                        System.out.println(request);
                        Photo mainPhoto = photoRepository.findByRequestIdAndIsMain(request.getId(), true);
                        System.out.println(mainPhoto);
                        Long photoId = mainPhoto.getId();

                        return RequestsListDTO.builder()
                                .request_id(request.getId())
                                .description(request.getDescription())
                                .status(request.getLocationIssue().getStatus())
                                .photo_id(photoId)
                                .build();

                    })
                    .collect(Collectors.toList());
        }
        return requestsListDTOList;
    }


}
