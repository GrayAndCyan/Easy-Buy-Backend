package com.mizore.easybuy.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.easybuy.mapper.TbCartMapper;
import com.mizore.easybuy.model.entity.TbCart;
import com.mizore.easybuy.service.base.ITbCartService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author AppearingOnNullday
* @description 针对表【tb_cart(购物车表)】的数据库操作Service实现
* @createDate 2024-05-08
*/
@Service
public class TbCartServiceImpl extends ServiceImpl<TbCartMapper, TbCart>
    implements ITbCartService {

    // 查询当前用户购物车中某件商品是否已经存在记录
    @Override
    public TbCart itemExist(Integer userId, Integer itemId) {
       TbCart tbCart = this.getOne(new QueryWrapper<TbCart>()
                .eq("user_id", userId)
                .eq("item_id", itemId));
        return tbCart;
    }

    // 查询当前用户全部购物车记录
    @Override
    public List<TbCart> search(Integer userId) {
        LambdaQueryWrapper<TbCart> query = new LambdaQueryWrapper<TbCart>()
                .eq(TbCart::getUserId, userId);
        return list(query);
    }
}




