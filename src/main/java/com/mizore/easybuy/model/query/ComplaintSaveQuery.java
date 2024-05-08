package com.mizore.easybuy.model.query;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComplaintSaveQuery {

    private int orderId;

    private String reason;
}
