package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.dto.ItemPageQueryDTO;
import com.mizore.easybuy.model.entity.ItemAndImage;
import com.mizore.easybuy.model.vo.PageResult;

public interface ItemService {

    /**
     * 根据商品id修改商品status
     * @param status
     * @param id
     */
    void UpdateItemStatus(Integer status, Integer id);

    /**
     * 商品分页查询
     * @param itemPageQueryDTO
     * @return
     */
    PageResult page(ItemPageQueryDTO itemPageQueryDTO);

    /**
     * 根据商品id查询
     * @param id
     * @return
     */
    ItemAndImage getById(Long id);
}
