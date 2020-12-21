package com.match.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 预约看房信息实体类
 *
 */
@Entity
@Table(name = "house_subscribe")
public class HouseSubscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //房源id
    @Column(name = "house_id")
    private Long houseId;
    //用户id
    @Column(name = "user_id")
    private Long userId;
    //房源发布者id
    @Column(name = "admin_id")
    private Long adminId;
    // 默认0 预约状态 1-加入待看清单 2-已预约看房时间 3-看房完成
    private int status;
    //数据创建时间
    @Column(name = "create_time")
    private Date createTime;
    //记录更新时间
    @Column(name = "last_update_time")
    private Date lastUpdateTime;
    //预约时间
    @Column(name = "order_time")
    private Date orderTime;
    //联系电话
    private String telephone;

    /**
     * 用户描述 踩坑 desc为MySQL保留字段 需要加转义
     */
    @Column(name = "`desc`")
    private String desc;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
