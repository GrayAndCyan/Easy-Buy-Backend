package com.mizore.easybuy.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mizore.easybuy.model.entity.TbItem;
import com.mizore.easybuy.mapper.TbItemMapper;
import com.mizore.easybuy.service.base.ITbItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
public class TbItemServiceImpl extends ServiceImpl<TbItemMapper, TbItem> implements ITbItemService {

    @Override
    public List<TbItem> conditionalGetItems(Integer categoryId, List<Integer> statuses, String keyword, int sellerId) {
        // 若没有传入条件则仅按照卖家id查询对应的商品
        LambdaQueryWrapper<TbItem> query = new LambdaQueryWrapper<TbItem>()
                .eq(TbItem::getSellerId, sellerId);
        // 可选择根据商品分类查询
        if(categoryId != null) {
            query.eq(TbItem::getCategoryId, categoryId);
        }
        // 可选择根据商品名称模糊查询
        if(keyword != null && !keyword.trim().isEmpty()) {
            query.like(TbItem::getTitle, keyword);
        }
        // 可选择根据商品状态查询
        if(CollectionUtil.isNotEmpty(statuses)) {
            query.in(TbItem::getStatus, statuses);
        }

        return list(query);
    }
}
