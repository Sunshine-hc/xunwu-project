package com.match.service.address;

import com.match.ApplicationTests;
import com.match.service.ServiceResult;
import com.match.service.house.IAddressService;
import com.match.service.search.BaiduMapLocation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AddressServiceTests extends ApplicationTests {
    @Autowired
    private IAddressService addressService;

    @Test
    public void testGetMapLocation(){
        String city = "北京市";
        String address = "北京市海淀区上地十街10号";
        ServiceResult<BaiduMapLocation> serviceResult = addressService.getBaiduMapLocation(city,address);

        Assert.assertTrue(serviceResult.getResult().getLongtiude() > 0);
        Assert.assertTrue(serviceResult.getResult().getLatitude() > 0);
        //System.out.println(serviceResult.getResult());
    }
}
