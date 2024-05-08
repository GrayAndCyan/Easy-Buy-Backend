package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.query.PageQuery;
import com.mizore.easybuy.model.vo.PageResult;

public interface AdminService {

    PageResult userList(PageQuery pageQuery);

    void sellerRemove(Integer id);
}
