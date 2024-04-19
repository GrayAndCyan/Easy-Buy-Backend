package com.mizore.easybuy.service.base.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mizore.easybuy.mapper.ItemMapper;
import com.mizore.easybuy.model.dto.ItemPageQueryDTO;
import com.mizore.easybuy.model.entity.ItemAndImage;
import com.mizore.easybuy.model.vo.PageResult;
import com.mizore.easybuy.service.base.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    /**
     * 根据商品id修改商品status
     * @param status
     * @param id
     */
    @Override
    public void UpdateItemStatus(Integer status, Integer id) {
        ItemAndImage itemAndImage = new ItemAndImage();
        itemAndImage.setId(id);
        itemAndImage.setStatus(status);
        itemAndImage.setMtime(LocalDateTime.now());
        itemMapper.UpdateItemStatus(itemAndImage);
    }

    /**
     * 商品分页查询
     * @param itemPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(ItemPageQueryDTO itemPageQueryDTO) {
        PageHelper.startPage(itemPageQueryDTO.getPageNum(), itemPageQueryDTO.getPageSize());
        Page<ItemAndImage> page = itemMapper.page(itemPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据商品id查询
     * @param id
     * @return
     */
    @Override
    public ItemAndImage getById(Long id) {
        ItemAndImage itemAndImage = itemMapper.getById(id);
        return itemAndImage;
    }


}
