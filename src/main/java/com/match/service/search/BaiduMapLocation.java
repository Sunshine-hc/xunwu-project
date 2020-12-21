package com.match.service.search;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 百度位置信息
 */
public class BaiduMapLocation {
    //经度
    @JsonProperty("lon")
    private double longtiude;

    //纬度
    @JsonProperty("lat")
    private double latitude;

    public double getLongtiude() {
        return longtiude;
    }

    public void setLongtiude(double longitude) {
        this.longtiude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
