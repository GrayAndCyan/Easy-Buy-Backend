package com.mizore.easybuy.service.base.impl;

import com.mizore.easybuy.model.entity.TbAddress;
import com.mizore.easybuy.mapper.TbAddressMapper;
import com.mizore.easybuy.service.base.ITbAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
public class TbAddressServiceImpl extends ServiceImpl<TbAddressMapper, TbAddress> implements ITbAddressService {
    @Override
    public int addAddress(TbAddress tbAddress) {
        return baseMapper.insert(tbAddress);
    }

    @Override
    public int updateAddress(TbAddress tbAddress) {
        return baseMapper.updateById(tbAddress);
    }

    @Override
    public int deleteAddress(Integer id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public TbAddress queryAddress(Integer id) {
        return baseMapper.selectById(id);
    }
}
