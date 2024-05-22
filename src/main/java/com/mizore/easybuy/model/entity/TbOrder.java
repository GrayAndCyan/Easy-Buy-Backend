package com.mizore.easybuy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@TableName("tb_order")
public class TbOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 下单用户id
     */
    private Integer userId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 地址id
     */
    private Integer addressId;

    /**
     * 订单总额
     */
    private BigDecimal totalAmount;

    private Integer sellerId;

    private String delinum;

    /**
     * 父订单id
     */
    private Integer parentId;

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * 软删标识，0：未删除，1：已删除
     */
    @TableLogic
    private Byte deleted;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    private LocalDateTime mtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setDelinum(String delinum){this.delinum=delinum;}

    public String getDeliveryNumber(){return delinum;}

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deletedFIELD) {
        this.deleted = deleted;
    }

    public LocalDateTime getCtime() {
        return ctime;
    }

    public void setCtime(LocalDateTime ctime) {
        this.ctime = ctime;
    }

    public LocalDateTime getMtime() {
        return mtime;
    }

    public void setMtime(LocalDateTime mtime) {
        this.mtime = mtime;
    }

    @Override
    public String toString() {
        return "TbOrder{" +
                "id=" + id +
                ", userId=" + userId +
                ", status=" + status +
                ", delinum+" + delinum +
                ", addressId=" + addressId +
                ", totalAmount=" + totalAmount +
                ", sellerId=" + sellerId +
                ", parentId=" + parentId +
                ", deleted=" + deleted +
                ", ctime=" + ctime +
                ", mtime=" + mtime +
                '}';
    }
}
