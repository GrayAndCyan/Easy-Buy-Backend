package com.mizore.easybuy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mizore.easybuy.model.entity.TbCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-08
 */
@Mapper
public interface TbCartMapper extends BaseMapper<TbCart> {
}
