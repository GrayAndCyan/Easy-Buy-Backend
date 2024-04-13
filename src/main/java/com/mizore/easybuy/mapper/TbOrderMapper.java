package com.mizore.easybuy.mapper;

import com.mizore.easybuy.model.entity.TbOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Mapper
public interface TbOrderMapper extends BaseMapper<TbOrder> {

}
