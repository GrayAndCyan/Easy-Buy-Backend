package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.dto.ItemPageQueryDTO;
import com.mizore.easybuy.model.entity.ItemAndImage;
import com.mizore.easybuy.model.entity.TbItem;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.PageResult;

import java.util.List;

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

    // 上架商品
    BaseVO<Object> addItem(TbItem tbItem, List<String> images);

    // 下架商品
    BaseVO<Object> deleteItem(Integer id) throws Exception;
}
