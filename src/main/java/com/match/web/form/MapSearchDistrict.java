package com.match.web.form;

/**
 * 用于封装小区房源加载数据信息
 * @author ：LY
 * @date ：Created in 2020/12/10 20:06
 * @modified By：
 */
public class MapSearchDistrict {
    private String cityEnName;
    private String orderBy = "lastUpdateTime";
    private String orderDirection = "desc";
    private int start = 0;
    private int size = 5;
    private String district;

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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getStart() {
        return start < 0 ? 0 : start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSize() {
        return size > 100 ? 100 : size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
