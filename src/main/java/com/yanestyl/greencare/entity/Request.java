package com.yanestyl.greencare.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "location_issue_id")
    private LocationIssue locationIssue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;


}
