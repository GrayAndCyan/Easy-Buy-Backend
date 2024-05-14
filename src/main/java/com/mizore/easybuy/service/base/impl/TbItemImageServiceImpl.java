package com.mizore.easybuy.service.base.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mizore.easybuy.model.entity.TbItemImage;
import com.mizore.easybuy.mapper.TbItemImageMapper;
import com.mizore.easybuy.service.base.ITbItemImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 商品图片表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
public class TbItemImageServiceImpl extends ServiceImpl<TbItemImageMapper, TbItemImage> implements ITbItemImageService {

    @Override
    public String getFirstImgUrl(Integer itemId) {
        LambdaQueryWrapper<TbItemImage> query = new LambdaQueryWrapper<TbItemImage>()
                .eq(TbItemImage::getItemId, itemId);
        List<TbItemImage> images = list(query);

        Optional<TbItemImage> first = images.stream()
                .min(Comparator.comparingInt(TbItemImage::getId));

        return first.isPresent() ? first.get().getUrl() : StrUtil.EMPTY;
    }
}
