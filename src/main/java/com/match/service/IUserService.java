package com.match.service;

import com.match.entity.User;
import com.match.web.dto.UserDTO;

/**
 * 用户服务
 */
public interface IUserService {

    User findUserByName(String userName);

    ServiceResult<UserDTO> findById(Long adminId);

    /**
     * 根据电话号码寻找用户
     * @param telephone
     * @return
     */
    User findUserByTelephone(String telephone);

    /**
     * 通过手机号  注册用户
     * @param telephone
     * @return
     */
    User addUserByPhone(String telephone);

    /**
     * 修改指定属性值
     * @param profile
     * @param value
     * @return
     */
    ServiceResult modifyUserProfile(String profile,String value);

}
