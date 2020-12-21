package com.match.web.controller.admin;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.match.base.ApiDataTableResponse;
import com.match.base.ApiResponse;
import com.match.base.HouseOperation;
import com.match.base.HouseStatus;
import com.match.entity.HouseDetail;
import com.match.entity.SupportAddress;
import com.match.service.IUserService;
import com.match.service.ServiceMultiResult;
import com.match.service.ServiceResult;
import com.match.service.house.IAddressService;
import com.match.service.house.IHouseService;
import com.match.service.house.IQiNiuService;
import com.match.web.dto.*;
import com.match.web.form.DatatableSearch;
import com.match.web.form.HouseForm;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private IQiNiuService qiNiuService;

    @Autowired
    private IAddressService addressService;

    @Autowired
    private IHouseService houseService;

    @Autowired
    private Gson gson;

    @Autowired
    private IUserService userService;

    /**
     * 后台管理中心
     * @return
     */
    @GetMapping("/admin/center")
    public String adminCenterPage(){
        return "admin/center";
    }

    /**
     * 欢迎页
     * @return
     */
    @GetMapping("/admin/welcome")
    public String welcomePage(){
        return "admin/welcome";
    }

    /**
     * 管理员登录页
     * @return
     */
    @GetMapping("/admin/login")
    public String adminLoginPage(){
        return "admin/login";
    }

    /**
     * 新增房源功能页
     * @return
     */
    @GetMapping("admin/add/house")
    public String addHousePage(){
        return "admin/house-add";
    }

    /**
     * 房源列表页
     * @return
     */
    @GetMapping("admin/house/list")
    public String houseListPage(){
        return "admin/house-list";
    }

    @PostMapping("admin/houses")
    @ResponseBody
    public ApiDataTableResponse houses(@ModelAttribute DatatableSearch searchBody){
        ServiceMultiResult<HouseDTO> result = houseService.adminQuery(searchBody);
        ApiDataTableResponse response = new ApiDataTableResponse(ApiResponse.Status.SUCCESS);
        response.setData(result.getResult());
        response.setRecordsFiltered(result.getTotal());
        response.setRecordsTotal(result.getTotal());
        response.setDraw(searchBody.getDraw());
        return response;
    }



    /**
     * 图片上传至七牛云
     * @param file
     * @return
     */
    @PostMapping(value = "admin/upload/photo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiResponse uploadPhoto(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){//如果文件为空
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }
        String fileName = file.getOriginalFilename();
        //
        try {
            //获取图片字节流
            InputStream inputStream = file.getInputStream();
            //调用方法以字节流方式将图片上传至七牛云
            Response response = qiNiuService.uploadFile(inputStream);
            if (response.isOK()){
                //上传成功就将信息响应给前端
                QiNiuPutRet ret = gson.fromJson(response.bodyString(), QiNiuPutRet.class);
                return ApiResponse.ofSuccess(ret);
            }else {
                return ApiResponse.ofMessage(response.statusCode,response.getInfo());
            }
        } catch (QiniuException e){
            Response response = e.response;
            try {
                return ApiResponse.ofMessage(response.statusCode,response.bodyString());
            } catch (QiniuException ex) {
                ex.printStackTrace();
                return ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 添加房源信息
     * @param houseForm
     * @param bindingResult
     * @return
     */
    @PostMapping("admin/add/house")
    @ResponseBody
    public ApiResponse addHouse(@Valid @ModelAttribute("form-house-add") HouseForm houseForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(),bindingResult.getAllErrors().get(0).getDefaultMessage(),null);
        }
        if (houseForm.getPhotos() == null || houseForm.getCover() == null){
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(),"必须上传图片");
        }
        Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(houseForm.getCityEnName(),houseForm.getRegionEnName());
        if (addressMap.keySet().size() != 2){
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }

        ServiceResult<HouseDTO> result = houseService.save(houseForm);
        if (result.isSuccess()){
            return ApiResponse.ofSuccess(result.getResult());
        }
        return ApiResponse.ofSuccess(ApiResponse.Status.NOT_VALID_PARAM);
    }

    /**
     * 房源信息编辑页
     * @param id
     * @param model
     * @return
     */
    @GetMapping("admin/house/edit")
    public String houseEditPage(@RequestParam(value = "id") Long id, Model model){
        if (id == null || id < 1){
            return "404";
        }
        ServiceResult<HouseDTO> serviceResult = houseService.findCompleteOne(id);
        if (!serviceResult.isSuccess()){
            return "404";
        }

        HouseDTO result = serviceResult.getResult();
        model.addAttribute("house",result);

        Map<SupportAddress.Level,SupportAddressDTO> addressMap = addressService.findCityAndRegion(
                result.getCityEnName(),result.getRegionEnName()
        );
        model.addAttribute("city",addressMap.get(SupportAddress.Level.CITY));
        model.addAttribute("region",addressMap.get(SupportAddress.Level.REGION));

        HouseDetailDTO detailDTO = result.getHouseDetail();
        ServiceResult<SubwayDTO> subwayDTOServiceResult = addressService.findSubway(detailDTO.getSubwayLineId());
        if (subwayDTOServiceResult.isSuccess()){
            model.addAttribute("subway",subwayDTOServiceResult.getResult());

        }

        ServiceResult<SubwayStationDTO> subwayStationDTOServiceResult = addressService.findSubwayStation(detailDTO.getSubwayStationId());
        if (subwayStationDTOServiceResult.isSuccess()){
            model.addAttribute("station",subwayStationDTOServiceResult.getResult());

        }
        return "admin/house-edit";
    }

    /**
     * 房源信息编辑接口
     * @return
     */
    @PostMapping("admin/house/edit")
    @ResponseBody
    public ApiResponse saveHouse(@Valid @ModelAttribute("form-house-edit") HouseForm houseForm,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(),bindingResult.getAllErrors().get(0).getDefaultMessage(),null);
        }

        Map<SupportAddress.Level,SupportAddressDTO> addressMap = addressService.findCityAndRegion(houseForm.getCityEnName(),houseForm.getRegionEnName());
        if (addressMap.keySet().size() != 2){
            return ApiResponse.ofSuccess(ApiResponse.Status.NOT_VALID_PARAM);
        }

        ServiceResult result = houseService.update(houseForm);
        if (result.isSuccess()){
            return ApiResponse.ofSuccess(null);
        }

        ApiResponse response = ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        response.setMessage(result.getMessage());
        return response;
    }

    /**
     * 移除图片接口
     * @param id
     * @return
     */
    @DeleteMapping("admin/house/photo")
    @ResponseBody
    public ApiResponse removeHousePhoto(@RequestParam(value = "id") Long id) {
        ServiceResult result = this.houseService.removePhoto(id);

        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 修改封面接口
     * @param coverId
     * @param targetId
     * @return
     */
    @PostMapping("admin/house/cover")
    @ResponseBody
    public ApiResponse updateCover(@RequestParam(value = "cover_id") Long coverId,
                                   @RequestParam(value = "target_id") Long targetId) {
        ServiceResult result = this.houseService.updateCover(coverId, targetId);

        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 增加标签接口
     * @param houseId
     * @param tag
     * @return
     */
    @PostMapping("admin/house/tag")
    @ResponseBody
    public ApiResponse addHouseTag(@RequestParam(value = "house_id") Long houseId,
                                   @RequestParam(value = "tag") String tag) {
        if (houseId < 1 || Strings.isNullOrEmpty(tag)) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        ServiceResult result = this.houseService.addTag(houseId, tag);
        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 移除标签接口
     * @param houseId
     * @param tag
     * @return
     */
    @DeleteMapping("admin/house/tag")
    @ResponseBody
    public ApiResponse removeHouseTag(@RequestParam(value = "house_id") Long houseId,
                                      @RequestParam(value = "tag") String tag) {
        if (houseId < 1 || Strings.isNullOrEmpty(tag)) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        ServiceResult result = this.houseService.removeTag(houseId, tag);
        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 审核接口
     * @param id
     * @param operation
     * @return
     */
    @PutMapping("admin/house/operate/{id}/{operation}")
    @ResponseBody
    public ApiResponse operateHouse(@PathVariable(value = "id") Long id,
                                    @PathVariable(value = "operation") int operation){
        if (id <= 0) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }

        ServiceResult result;

        switch (operation){
            case HouseOperation.PASS:
                result = this.houseService.updateStatus(id, HouseStatus.PASSES.getValue());
                break;
            case HouseOperation.PULL_OUT:
                result = this.houseService.updateStatus(id, HouseStatus.NOT_AUDITED.getValue());
                break;
            case HouseOperation.DELETE:
                result = this.houseService.updateStatus(id, HouseStatus.DELETED.getValue());
                break;
            case HouseOperation.RENT:
                result = this.houseService.updateStatus(id, HouseStatus.RENTED.getValue());
                break;
            default:
                return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        if (result.isSuccess()){
            return ApiResponse.ofSuccess(null);

        }

        return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(),result.getMessage());
    }

    /**
     * 预约列表页
     * @return
     */
    @GetMapping("admin/house/subscribe")
    public String subscribePage(){
        return "admin/subscribe";
    }

    @GetMapping("admin/house/subscribe/list")
    @ResponseBody
    public ApiResponse subscribeList(@RequestParam(value = "draw") int draw,
                                     @RequestParam(value = "start") int start,
                                     @RequestParam(value = "length") int size){
        ServiceMultiResult<Pair<HouseDTO,HouseSubscribeDTO>> result = houseService.findSubscribeList(start,size);
        ApiDataTableResponse response = new ApiDataTableResponse(ApiResponse.Status.SUCCESS);
        response.setData(result.getResult());
        response.setDraw(draw);
        response.setRecordsFiltered(result.getTotal());
        response.setRecordsTotal(result.getTotal());
        return response;
    }

    @GetMapping("admin/user/{userId}")
    @ResponseBody
    public ApiResponse getUserInfo(@PathVariable(value = "userId") Long userId){
        if (userId == null || userId < 1){
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        ServiceResult<UserDTO> serviceResult = userService.findById(userId);
        if (!serviceResult.isSuccess()){
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_FOUND);
        } else {
            return ApiResponse.ofSuccess(serviceResult.getResult());
        }
    }

    @PostMapping("admin/finish/subscribe")
    @ResponseBody
    public ApiResponse finishSubscribe(@RequestParam(value = "house_id") Long houseId){
        if (houseId < 1){
           return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }
        ServiceResult serviceResult = houseService.finishSubscribe(houseId);
        if (serviceResult.isSuccess()){
            return ApiResponse.ofSuccess("");
        } else {
            return ApiResponse.ofMessage(ApiResponse.Status.BAD_REQUEST.getCode(),serviceResult.getMessage());
        }
    }

}
