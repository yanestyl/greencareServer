package com.yanestyl.greencare.services.impl;

import com.yanestyl.greencare.entity.LocationIssue;
import com.yanestyl.greencare.repository.LocationIssueRepository;
import com.yanestyl.greencare.repository.RequestRepository;
import com.yanestyl.greencare.services.LocationIssueService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationIssueServiceImpl implements LocationIssueService {

    private final LocationIssueRepository locationIssueRepository;
    private final RequestRepository requestRepository;


    @Override
    public Long getRequestsCountByLocationIssueStatus(int issueStatus) {
        return requestRepository.countByLocationIssueStatus(issueStatus);
    }



    @Override
    public LocationIssue getLocationIssueOrCreateIfNotExists(double lng, double lat, double degreesFor100Meters) {
        // Попробуем найти проблемное место по геолокации
        // Попробуем найти проблемное место в заданном диапазоне по геолокации
        Optional<LocationIssue> existingLocationIssue = locationIssueRepository.findByLngBetweenAndLatBetween(
                lng - degreesFor100Meters, lng + degreesFor100Meters,
                lat - degreesFor100Meters, lat + degreesFor100Meters
        );

        return existingLocationIssue.orElseGet(() -> createNewLocationIssue(lng, lat));
    }

    private LocationIssue createNewLocationIssue(double lng, double lat) {
        // задаем первоначальный статус
        int status = 0;

        // Создаем новое проблемное место
        LocationIssue newLocationIssue = LocationIssue.builder()
                .lng(lng)
                .lat(lat)
                .status(status)
                .build();

        // Сохраняем в базе данных
        return locationIssueRepository.save(newLocationIssue);
    }


}
