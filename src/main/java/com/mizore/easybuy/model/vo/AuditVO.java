package com.mizore.easybuy.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditVO {

    /**
     * 自增主键ID
     */
    private Object id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 客体id
     */
    private Integer objectId;

    /**
     * 客体类型
     */
    private String objectType;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 细节信息
     */
    private String detail;

    /**
     * 发生时间
     */
    private LocalDateTime actionTime;

}
