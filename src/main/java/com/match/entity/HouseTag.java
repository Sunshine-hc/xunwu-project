package com.match.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *房屋标签映射关系实体类
 */
@Entity
@Table(name = "house_tag")
public class HouseTag {
    //标签id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //房屋id
    @Column(name = "house_id")
    private Long houseId;
    //标签名
    private String name;

    public HouseTag() {
    }

    public HouseTag(Long houseId, String name) {
        this.houseId = houseId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
