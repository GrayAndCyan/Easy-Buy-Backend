package com.mizore.easybuy.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbAddress;
import com.mizore.easybuy.mapper.TbAddressMapper;
import com.mizore.easybuy.service.base.ITbAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.easybuy.utils.UserHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 查找或插入地址，并返回地址id
     * @param userid 用户id
     * @param address 地址描述
     * @param username 收货人
     * @param phone 手机号
     * @return 地址id
     */
    public int findOrInsertAddr(Integer userid, String address, String username, String phone) {
        // 根据用户id、地址描述以及收货人，查询地址信息是否已存在
        TbAddress tbAddress = this.getOne(new QueryWrapper<TbAddress>()
                .eq("user_id", userid)
                .eq("addr_desc", address)
                .eq("addr_username", username));
        // 地址已存在，返回地址id
        if(tbAddress != null){
            return tbAddress.getId();
        }
        // 地址不存在，向地址表中插入地址并返回地址id todo 暂时没管默认地址的事情
        TbAddress newAddr = new TbAddress();
        newAddr.setUserId(userid);
        newAddr.setAddrDesc(address);
        newAddr.setAddrUsername(username);
        newAddr.setAddrPhone(phone);
        save(newAddr);  // 向地址表中插入地址
        return newAddr.getId(); // 返回地址id
    }

    @Override
    public List<TbAddress> searchadd(){
        UserDTO user = UserHolder.get();
        Integer userId = user.getId();

        LambdaQueryWrapper<TbAddress> query = new LambdaQueryWrapper<TbAddress>()
                .eq(TbAddress::getUserId,userId);
        return list(query);
    }
}
