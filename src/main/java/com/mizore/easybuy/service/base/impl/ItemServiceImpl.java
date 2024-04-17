package com.mizore.easybuy.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mizore.easybuy.mapper.ItemMapper;
import com.mizore.easybuy.model.dto.ItemPageQueryDTO;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.ItemAndImage;
import com.mizore.easybuy.model.entity.TbItem;
import com.mizore.easybuy.model.entity.TbItemImage;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.PageResult;
import com.mizore.easybuy.service.base.ITbItemImageService;
import com.mizore.easybuy.service.base.ITbItemService;
import com.mizore.easybuy.service.base.ITbUserService;
import com.mizore.easybuy.service.base.ItemService;
import com.mizore.easybuy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ITbItemService tbItemService;

    @Autowired
    private ITbItemImageService tbItemImageService;

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

    /**
     * 上架商品
     */
    public BaseVO<Object> addItem(TbItem tbItem, List<String> images) {
        BaseVO<Object> baseVO = new BaseVO<>();
        // 获取当前登录用户
        UserDTO user = UserHolder.get();
        // todo 普通买家用户不能添加商品
        // 设置上架商品对应的卖家id
        tbItem.setSellerId(user.getId());
        // 将新增商品插入商品表
        tbItemService.save(tbItem);
        // 获得新增商品id
        Integer itemId = tbItem.getId();
        // 依次在商品-图片表中插入商品对应的图片信息
        for(String image: images) {
            TbItemImage tbItemImage = new TbItemImage();
            tbItemImage.setItemId(itemId);
            tbItemImage.setUrl(image);
            // 插入商品-图片表
            tbItemImageService.save(tbItemImage);
        }
        return baseVO.success();
    }

    /**
     * 根据商品id，下架指定商品
     */
    @Transactional
    public BaseVO<Object> deleteItem(Integer id) throws RuntimeException {
        BaseVO<Object> baseVO = new BaseVO<>();
        // 判断商品是否存在
        TbItem tbItem = tbItemService.getById(id);
        if(tbItem == null) {
            throw new RuntimeException("item not exist");
        }
        // 从商品表中删除商品信息
        tbItemService.removeById(id);
        // 删除商品-图片表中所有相关条目
        QueryWrapper<TbItemImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_id", id);  // 设置删除条件
        tbItemImageService.remove(queryWrapper);  // 执行删除操作
        // 返回删除成功信息
        return baseVO.success();
    }
}
