package com.mizore.easybuy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// 学单词： 投诉者与被投诉者 -  Complainant and Respondent
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintSearchVO {

    private Integer complaintId;

    /**
     * 投诉者id
     */
    private Integer complainantId;

    // 投诉者username，如果是卖家被投诉 就是店铺名；买家则是用户名
    private String complainantName;

    // 被投诉者id
    private Integer respondentId;

    // 被投诉者name，如果是卖家被投诉 就是店铺名；买家则是用户名
    private String respondentName;

    /**
     * 对应的订单id
     */
    private Integer orderId;

    /**
     * 投诉原因
     */
    private String reason;

    /**
     * 投诉单状态
     */
    private Integer statusCode;

    private String statusDesc;

    /**
     * 投诉单类型
     */
    private Integer typeCode;

    private String typeDesc;

    /**
     * 创建时间
     */
    private Date ctime;
}
