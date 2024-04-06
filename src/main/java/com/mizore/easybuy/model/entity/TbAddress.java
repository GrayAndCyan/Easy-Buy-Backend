package com.mizore.easybuy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 地址表
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@TableName("tb_address")
public class TbAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 该地址对应的用户账号id
     */
    private Integer userId;

    /**
     * 地址描述
     */
    private String addrDesc;

    /**
     * 地址对应的姓名
     */
    private String addrUsername;

    /**
     * 手机号
     */
    private String addrPhone;

    /**
     * 标识是否为默认地址，0：非默认，1：默认
     */
    private Byte defaultFlag;

    /**
     * 软删标识，0：未删除，1：已删除
     */
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

    public String getAddrDesc() {
        return addrDesc;
    }

    public void setAddrDesc(String addrDesc) {
        this.addrDesc = addrDesc;
    }

    public String getAddrUsername() {
        return addrUsername;
    }

    public void setAddrUsername(String addrUsername) {
        this.addrUsername = addrUsername;
    }

    public String getAddrPhone() {
        return addrPhone;
    }

    public void setAddrPhone(String addrPhone) {
        this.addrPhone = addrPhone;
    }

    public Byte getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Byte defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
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
        return "TbAddress{" +
            "id = " + id +
            ", userId = " + userId +
            ", addrDesc = " + addrDesc +
            ", addrUsername = " + addrUsername +
            ", addrPhone = " + addrPhone +
            ", defaultFlag = " + defaultFlag +
            ", deleted = " + deleted +
            ", ctime = " + ctime +
            ", mtime = " + mtime +
        "}";
    }
}
