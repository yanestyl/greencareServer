package com.yanestyl.greencare.repository;

import com.yanestyl.greencare.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByRequestId(Long requestId);
    Photo findByRequestIdAndIsMain(Long requestId, boolean isMain);

}
