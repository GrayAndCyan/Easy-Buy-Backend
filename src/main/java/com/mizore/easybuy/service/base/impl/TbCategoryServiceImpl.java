package com.mizore.easybuy.service.base.impl;

import com.mizore.easybuy.model.entity.TbCategory;
import com.mizore.easybuy.mapper.TbCategoryMapper;
import com.mizore.easybuy.service.base.ITbCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
public class TbCategoryServiceImpl extends ServiceImpl<TbCategoryMapper, TbCategory> implements ITbCategoryService {
    @Override
    public int addCate(TbCategory tbCategory){
        return baseMapper.insert(tbCategory);
    }

    @Override
    public int updateCate(TbCategory tbCategory){
        return baseMapper.updateById(tbCategory);
    }

    @Override
    public int deleteCate(Integer id){
        return baseMapper.deleteById(id);
    }

    @Override
    public TbCategory queryCate(Integer id){
        return baseMapper.selectById(id);
    }
}
