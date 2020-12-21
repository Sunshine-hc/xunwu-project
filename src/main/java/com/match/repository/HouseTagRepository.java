package com.match.repository;

import com.match.entity.HouseTag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 */
public interface HouseTagRepository extends CrudRepository<HouseTag,Long> {

    List<HouseTag> findAllByHouseId(Long id);

    HouseTag findByNameAndHouseId(String tag, Long houseId);

    List<HouseTag> findAllByHouseIdIn(List<Long> houseIds);

}
