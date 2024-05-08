package com.mizore.easybuy.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.easybuy.mapper.TbCartMapper;
import com.mizore.easybuy.model.entity.TbCart;
import com.mizore.easybuy.service.base.ITbCartService;
import org.springframework.stereotype.Service;

/**
* @author AppearingOnNullday
* @description 针对表【tb_cart(购物车表)】的数据库操作Service实现
* @createDate 2024-05-08
*/
@Service
public class TbCartServiceImpl extends ServiceImpl<TbCartMapper, TbCart>
    implements ITbCartService {

}




