package com.mizore.easybuy.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mizore.easybuy.model.entity.TbCart;

import java.util.List;

/**
* @author AppearingOnNullday
* @description 针对表【tb_cart(购物车表)】的数据库操作Service
* @createDate 2024-05-08
*/
public interface ITbCartService extends IService<TbCart> {

    // 查询当前用户购物车中某件商品是否已经存在记录
    TbCart itemExist(Integer userId, Integer itemId);

    // 查询当前用户全部购物车记录
    List<TbCart> search(Integer userId);
}
