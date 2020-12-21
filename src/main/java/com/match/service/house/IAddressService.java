package com.match.service.house;

import com.match.entity.SupportAddress;
import com.match.service.ServiceMultiResult;
import com.match.service.ServiceResult;
import com.match.service.search.BaiduMapLocation;
import com.match.web.dto.SubwayDTO;
import com.match.web.dto.SubwayStationDTO;
import com.match.web.dto.SupportAddressDTO;

import java.util.List;
import java.util.Map;

/**
 * 地址服务接口
 */
public interface IAddressService {
    /**
     * 获取支持城市列表
     * @return
     */
    ServiceMultiResult<SupportAddressDTO> findAllCities();

    /**
     * 根据英文简写获取具体区域的信息
     * @param cityEnName
     * @param regionEnName
     * @return
     */
    Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName);

    /**
     * 根据城市英文简写获取该城市所有支持的区域信息
     * @param cityName
     * @return
     */
    ServiceMultiResult findAllRegionsByCityName(String cityName);

    /**
     * 获取该城市所有的地铁线路
     * @param cityEnName
     * @return
     */
    List<SubwayDTO> findAllSubwayByCity(String cityEnName);

    /**
     * 获取地铁线路所有的站点
     * @param subwayId
     * @return
     */
    List<SubwayStationDTO> findAllStationBySubway(Long subwayId);

    /**
     * 获取地铁线信息
     * @param subwayId
     * @return
     */
    ServiceResult<SubwayDTO> findSubway(Long subwayId);

    /**
     * 获取地铁站点信息
     * @param stationId
     * @return
     */
    ServiceResult<SubwayStationDTO> findSubwayStation(Long stationId);

    /**
     * 根据城市英文简写获取城市详细信息
     * @param cityEnName
     * @return
     */
    ServiceResult<SupportAddressDTO> findCity(String cityEnName);

    /**
     * 根据城市以及具体地位获取百度地图的经纬度
     * @param city
     * @return
     */
    ServiceResult<BaiduMapLocation> getBaiduMapLocation(String city,String address);

    /**
     * 上传百度LBS数据
     */
    ServiceResult lbsUpload(BaiduMapLocation location,String title,String address,

                            long houseId,int price,int area);

    /**
     * 移除LBS数据
     * @param houseId
     * @return
     */
    ServiceResult removeLbs(Long houseId);
}
