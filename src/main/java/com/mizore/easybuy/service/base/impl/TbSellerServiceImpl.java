package com.mizore.easybuy.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mizore.easybuy.model.entity.TbSeller;
import com.mizore.easybuy.mapper.TbSellerMapper;
import com.mizore.easybuy.service.base.ITbSellerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 店铺表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
@Slf4j
public class TbSellerServiceImpl extends ServiceImpl<TbSellerMapper, TbSeller> implements ITbSellerService {

    @Override
    public TbSeller getByOwner(int ownerId) {
        LambdaQueryWrapper<TbSeller> query = new LambdaQueryWrapper<TbSeller>()
                .eq(TbSeller::getUserId, ownerId);
        List<TbSeller> res = list(query);
        if (res != null && res.size() > 1) {
            log.error("存在脏数据 seller: {}", Arrays.toString(res.toArray()));
        }
        return CollectionUtil.isEmpty(res) ? null : res.get(0);
    }
}
