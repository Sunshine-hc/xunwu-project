package com.match.web.form;

/**
 * @author ：LY
 * @date ：Created in 2020/11/22 20:01
 * @modified By：
 */
public class HouseBuketRegionDTO {
    /**
     * 聚合bucket的key
     */
    private String key;

    private String region;
    /**
     * 聚合结果值
     */
    private long count;

    public HouseBuketRegionDTO(String key, String region, long count) {
        this.key = key;
        this.region = region;
        this.count = count;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
