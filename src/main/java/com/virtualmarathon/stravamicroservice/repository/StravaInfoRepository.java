package com.virtualmarathon.stravamicroservice.repository;

import com.virtualmarathon.stravamicroservice.entity.StravaInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StravaInfoRepository extends JpaRepository<StravaInfo,String> {
}
