package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.entity.TbCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
public interface ITbCategoryService extends IService<TbCategory> {

    int addCate(TbCategory tbCategory);

    int updateCate(TbCategory tbCategory);

    int deleteCate(Integer id);

    TbCategory queryCate(Integer id);
}
