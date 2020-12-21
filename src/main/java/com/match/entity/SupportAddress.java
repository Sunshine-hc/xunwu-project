package com.match.entity;

import javax.persistence.*;

/**
 *支持城市 及 对应区域
 */
@Entity
@Table(name = "support_address")
public class SupportAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //上一级行政单位名
    @Column(name = "belong_to")
    private String belongTo;
    //行政单位英文名缩写
    @Column(name = "en_name")
    private String enName;
    //行政单位中文名
    @Column(name = "cn_name")
    private String cnName;
    //行政级别 市-city 地区-region
    private String level;

    @Column(name = "baidu_map_lng")
    private double baiduMapLongtiude;

    @Column(name = "baidu_map_lat")
    private double baiduMapLatitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getBaiduMapLongtiude() {
        return baiduMapLongtiude;
    }

    public void setBaiduMapLongtiude(double baiduMapLongtiude) {
        this.baiduMapLongtiude = baiduMapLongtiude;
    }

    public double getBaiduMapLatitude() {
        return baiduMapLatitude;
    }

    public void setBaiduMapLatitude(double baiduMapLatitude) {
        this.baiduMapLatitude = baiduMapLatitude;
    }

    /**
     * 行政级别定义
     */
    public enum Level{
        CITY("city"),
        REGION("region");

        private String value;

        Level(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Level of(String value){
            for (Level level : Level.values()){
                if (level.getValue().equals(value)){
                    return level;
                }
            }

            throw new IllegalArgumentException();
        }
    }
}
