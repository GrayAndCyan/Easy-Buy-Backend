package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.entity.TbItemImage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品图片表 服务类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
public interface ITbItemImageService extends IService<TbItemImage> {

    String getFirstImgUrl(Integer itemId);

}
