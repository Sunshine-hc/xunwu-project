package com.match.base;

import com.google.common.collect.Sets;
import org.springframework.data.domain.Sort;

import java.util.Set;

/**
 * 排序生成器
 */
public class HouseSort {
    public static final String DEFAULT_SORT_KEY = "lastUpdateTime";

    public static final String DISTANCE_TO_SUBWAY_KEY = "distanceToSubway";

    private static final Set<String> SORT_KEYS = Sets.newHashSet(
            DEFAULT_SORT_KEY,
            "createTime",
            "price",
            "area",
            DISTANCE_TO_SUBWAY_KEY
    );

    public static Sort generateSort(String key,String directionKey){
        key = getSortkey(key);

        Sort.Direction direction = Sort.Direction.fromStringOrNull(directionKey);
        if (direction == null){
            direction = Sort.Direction.DESC;
        }

        return new Sort(direction,key);
    }

    public static String getSortkey(String key){
        if (!SORT_KEYS.contains(key)){
            key = DEFAULT_SORT_KEY;
        }

        return key;
    }
}
