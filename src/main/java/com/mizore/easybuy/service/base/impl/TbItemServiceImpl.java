package com.mizore.easybuy.service.base.impl;

import com.mizore.easybuy.model.entity.TbItem;
import com.mizore.easybuy.mapper.TbItemMapper;
import com.mizore.easybuy.service.base.ITbItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
