package com.yanestyl.greencare.controllers;

import com.yanestyl.greencare.entity.LocationIssue;
import com.yanestyl.greencare.services.LocationIssueService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/location-issues")
@AllArgsConstructor
public class LocationIssueController {

    private LocationIssueService locationIssueService;


    @GetMapping("/requests-count")
    public ResponseEntity<Long> getRequestsCount() {
        // для теста
        int issueStatus = 1;
        long count = locationIssueService.getRequestsCountByLocationIssueStatus(issueStatus);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}