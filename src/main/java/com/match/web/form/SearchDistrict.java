package com.match.web.form;

/**
 * 用于封装小区房源加载数据信息
 */
public class SearchDistrict {
    private String cityEnName;
    private String orderBy = "lastUpdateTime";
    private String orderDirection = "desc";
    /**
     * 地图缩放级别
     */
    private int level = 10;
    /**
     * 左上角
     */
    private Double leftLongitude;
    private Double leftLatitude;

    /**
     * 右下角
     */
    private Double rightLongitude;
    private Double rightLatitude;

    public String getCityEnName() {
        return cityEnName;
    }

    public void setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Double getLeftLongitude() {
        return leftLongitude;
    }

    public void setLeftLongitude(Double leftLongitude) {
        this.leftLongitude = leftLongitude;
    }

    public Double getLeftLatitude() {
        return leftLatitude;
    }

    public void setLeftLatitude(Double leftLatitude) {
        this.leftLatitude = leftLatitude;
    }

    public Double getRightLongitude() {
        return rightLongitude;
    }

    public void setRightLongitude(Double rightLongitude) {
        this.rightLongitude = rightLongitude;
    }

    public Double getRightLatitude() {
        return rightLatitude;
    }

    public void setRightLatitude(Double rightLatitude) {
        this.rightLatitude = rightLatitude;
    }
}
