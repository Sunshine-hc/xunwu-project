package com.match.service.house;

import com.match.base.ApiResponse;
import com.match.base.HouseSubscribeStatus;
import com.match.service.ServiceMultiResult;
import com.match.service.ServiceResult;
import com.match.web.dto.HouseDTO;
import com.match.web.dto.HouseSubscribeDTO;
import com.match.web.form.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 房屋管理服务接口
 */
public interface IHouseService {
    /**
     * 添加房源
     * @param houseForm
     * @return
     */
    ServiceResult<HouseDTO> save(HouseForm houseForm);

    /**
     * 修改房源信息
     * @param houseForm
     * @return
     */
    ServiceResult update(HouseForm houseForm);

    /**
     *
     * @param searchBody
     * @return
     */
    ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody);

    /**
     * 根据id查询完整房源信息
     * @param id
     * @return
     */
    ServiceResult<HouseDTO> findCompleteOne(Long id);

    /**
     * 移除图片
     * @param id
     * @return
     */
    ServiceResult removePhoto(Long id);

    /**
     * 修改封面
     * @param coverId
     * @param targetId
     * @return
     */
    ServiceResult updateCover(Long coverId, Long targetId);

    /**
     * 增加标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult addTag(Long houseId, String tag);

    /**
     * 移除标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult removeTag(Long houseId, String tag);

    /**
     * 更新房源状态
     * @param id
     * @param status
     * @return
     */
    ServiceResult updateStatus(Long id,int status);

    /**
     * 查询房源信息集
     * @param rentSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> query(RentSearch rentSearch);

    /**
     * 全地图查询
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> wholeMapQuery(MapSearch mapSearch);

    /**
     * 通过小区查询房源
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> districtMapQuery(MapSearchDistrict mapSearch);

    /**
     * 精确范围数据查询
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> boundMapQuery(MapSearch mapSearch);

    /**
     * 加入预约清单
     * @param houseId
     * @return
     */
    ServiceResult addSubscribeOrder(Long houseId);

    /**
     * 获取对应状态的预约列表
     * @param status
     * @param start
     * @param size
     * @return
     */
    ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> querySubscribeList(HouseSubscribeStatus status, int start, int size);

    /**
     * 预约看房时间
     * @param houseId
     * @param orderTime
     * @param telephone
     * @param desc
     * @return
     */
    ServiceResult subscribe(Long houseId, Date orderTime,String telephone,String desc);

    /**
     * 取消预约
     * @param houseId
     * @return
     */
    ServiceResult cancelSubscribe(Long houseId);

    /**
     * 管理员查询预约信息接口
     * @param start
     * @param size
     * @return
     */
    ServiceMultiResult<Pair<HouseDTO,HouseSubscribeDTO>> findSubscribeList(int start, int size);

    /**
     * 完成预约
     * @param houseId
     * @return
     */
    ServiceResult finishSubscribe(Long houseId);

    /**
     * 通过小区名拿到地区英文缩写
     * @param district
     * @return
     */
    ServiceResult findRegion(String district);
}
