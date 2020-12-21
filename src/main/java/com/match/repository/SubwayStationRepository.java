package com.match.repository;

import com.match.entity.SubwayStation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 */
public interface SubwayStationRepository extends CrudRepository<SubwayStation,Long> {
    List<SubwayStation> findAllBySubwayId(Long subwayId);
}
