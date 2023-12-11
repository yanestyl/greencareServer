package com.yanestyl.greencare.repository;

import com.yanestyl.greencare.entity.LocationIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Optional;

@Repository
public interface LocationIssueRepository extends JpaRepository<LocationIssue, Long> {

    @Query("SELECT li FROM LocationIssue li WHERE li.lng BETWEEN :minLng AND :maxLng AND li.lat BETWEEN :minLat AND :maxLat")
    Optional<LocationIssue> findByLngBetweenAndLatBetween(@Param("minLng") double minLng, @Param("maxLng") double maxLng,
                                                          @Param("minLat") double minLat, @Param("maxLat") double maxLat);

}
