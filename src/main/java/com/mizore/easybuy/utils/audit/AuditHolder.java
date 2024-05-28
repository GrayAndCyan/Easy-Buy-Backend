package com.mizore.easybuy.utils.audit;

import com.mizore.easybuy.model.entity.TbAudit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuditHolder {

    private final ThreadLocal<TbAudit> tl = new ThreadLocal<>();

    public void save(TbAudit audit) {
        tl.set(audit);
    }

    public TbAudit get() {
        return tl.get();
    }

    public void remove() {
        tl.remove();
    }

}
