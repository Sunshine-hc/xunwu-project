package com.match.service.search;

import com.match.service.ServiceMultiResult;
import com.match.service.ServiceResult;
import com.match.web.form.MapSearch;
import com.match.web.form.MapSearchDistrict;
import com.match.web.form.RentSearch;
import com.match.web.form.SearchDistrict;


import java.util.List;

/**
 * 检索接口
 */
public interface ISearchService {
    /**
     * 索引目标房源
     * @param houseId
     */
    void index(Long houseId);

    /**
     * 移除房源索引
     * @param houseId
     */
    void remove(Long houseId);

    /**
     * 查询房源接口
     * @param rentSearch
     * @return
     */
    ServiceMultiResult<Long> query(RentSearch rentSearch);

    /**
     * 获取补全建议关键字（搜索框输入时）
     * @return
     */
    ServiceResult<List<String>> suggest(String prefix);

    /**
     * 聚合特定小区的房间数
     * @param cityEnName
     * @param regionEnName
     * @param district
     * @return
     */
    ServiceResult<Long> aggregateDistrictHouse(String cityEnName,String regionEnName,String district);

    /**
     * 聚合城市数据
     * @param cityEnName
     * @return
     */
    ServiceMultiResult<HouseBucketDTO> mapAggregate(String cityEnName);

    /**
     * 城市级别查询
     * @return
     */
    ServiceMultiResult<Long> mapQuery(String cityEnName,String orderBy,String orderDirection,int start,int size);

    /**
     * 精确范围数据查询
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<Long> mapQuery(MapSearch mapSearch);

    /**
     * 通过城市与小区查询房源信息
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<Long> mapQuery(MapSearchDistrict mapSearch);

    /**
     * 聚合所有小区的房源数量
     * @param searchDistrict
     * @return
     */
    ServiceMultiResult<HouseBucketDTO> mapAggregateDistrict(SearchDistrict searchDistrict);
}
