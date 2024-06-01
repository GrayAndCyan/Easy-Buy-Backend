package com.mizore.easybuy.model.vo;

import com.mizore.easybuy.model.enums.EventTypeEnums;
import com.mizore.easybuy.model.enums.ObjectTypeEnums;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 写个懒加载的单例，基于静态内部类
@Data
public class AuditEnumVO {

    private AuditEnumVO() {}

    private static class Inner {
        private static final AuditEnumVO INS;

        static {
            AuditEnumVO auditEnumVO = new AuditEnumVO();
            auditEnumVO.setEventTypes(Arrays.stream(EventTypeEnums.values())
                    .map(EventTypeEnums::getDesc)
                    .collect(Collectors.toList()));
            auditEnumVO.setObjectTypes(Arrays.stream(ObjectTypeEnums.values())
                    .map(ObjectTypeEnums::getDesc)
                    .collect(Collectors.toList()));
            INS = auditEnumVO;
        }
    }

    public static AuditEnumVO getInstance() {
        return Inner.INS;
    }

    private List<String> eventTypes;

    private List<String> objectTypes;

}
