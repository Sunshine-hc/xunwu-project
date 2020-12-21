package com.match.web.controller.user;

import com.match.base.ApiResponse;
import com.match.base.HouseSubscribeStatus;
import com.match.base.LoginUserUtil;
import com.match.service.IUserService;
import com.match.service.ServiceMultiResult;
import com.match.service.ServiceResult;
import com.match.service.house.IHouseService;
import com.match.web.dto.HouseDTO;
import com.match.web.dto.HouseSubscribeDTO;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IHouseService houseService;

    /**
     * 用户登录页
     * @return
     */
    @GetMapping("/user/login")
    public String loginPage(){
        return "user/login";
    }

    /**
     * 用户个人中心
     * @return
     */
    @GetMapping("user/center")
    public String centerPage(){
        return "user/center";
    }

    @PostMapping(value = "api/user/info")
    @ResponseBody
    public ApiResponse updateUserInfo(@RequestParam(value = "profile")String profile,
                                      @RequestParam(value = "value") String value){
        if (value.isEmpty()){
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }
        if ("email".equals(profile) && !LoginUserUtil.checkEmail(value)){
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST,"不支持的邮箱格式");
        }

        ServiceResult result = userService.modifyUserProfile(profile,value);
        if (result.isSuccess()){
            return ApiResponse.ofSuccess("");
        }else {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST,result.getMessage());
        }
    }

    @PostMapping(value = "api/user/house/subscribe")
    @ResponseBody
    public ApiResponse subscribeHouse(@RequestParam(value = "house_id") Long houseId){
        ServiceResult result = houseService.addSubscribeOrder(houseId);
        if (result.isSuccess()){
            return ApiResponse.ofSuccess("");
        } else {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST,result.getMessage());
        }
    }

    @GetMapping(value = "api/user/house/subscribe/list")
    @ResponseBody
    public ApiResponse  subscribeList(@RequestParam(value = "start",defaultValue = "0") int start,
                                      @RequestParam(value = "size",defaultValue = "3") int size,
                                      @RequestParam(value = "status") int status){
        ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> result = houseService.querySubscribeList(HouseSubscribeStatus.of(status),start,size);

        if (result.getResultSize() == 0){
            return ApiResponse.ofSuccess(result.getResult());
        }
        ApiResponse response = ApiResponse.ofSuccess(result.getResult());
        response.setMore(result.getTotal() > (start + size));
        return response;
    }

    @PostMapping(value = "api/user/house/subscribe/date")
    @ResponseBody
    public ApiResponse subscribeDate(
            @RequestParam(value = "houseId") Long houseId,
            @RequestParam(value = "orderTime") @DateTimeFormat(pattern = "yyyy-MM-dd")Date orderTim,
            @RequestParam(value = "desc",required = false) String desc,
            @RequestParam(value = "telephone") String telephone
            ){
        if (orderTim == null){
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST,"请选择预约时间");
        }
        if (!LoginUserUtil.checkTelephone(telephone)){
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST,"手机格式不正确");
        }

        ServiceResult serviceResult = houseService.subscribe(houseId,orderTim,telephone,desc);
        if (serviceResult.isSuccess()){
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST,serviceResult.getMessage());
        }
    }

    @DeleteMapping(value = "api/user/house/subscribe")
    @ResponseBody
    public ApiResponse cancelSubs(@RequestParam(value = "houseId")Long houseId){
        ServiceResult serviceResult = houseService.cancelSubscribe(houseId);
        if (serviceResult.isSuccess()){
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST,serviceResult.getMessage());
        }
    }
}
