package com.yanestyl.greencare.repository;

import com.yanestyl.greencare.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    // Метод для получения всех заявок пользователя
    List<Request> findByUserId(Long userId);

    Long countByLocationIssueStatus(int issueStatus);


}
