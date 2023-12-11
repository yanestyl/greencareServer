package com.yanestyl.greencare.services;

import com.yanestyl.greencare.entity.LocationIssue;

import java.awt.*;
import java.util.Optional;

public interface LocationIssueService {

    Long getRequestsCountByLocationIssueStatus(int issueStatus);


    LocationIssue getLocationIssueOrCreateIfNotExists(double lng, double lat, double degreesFor100Meters);

}
