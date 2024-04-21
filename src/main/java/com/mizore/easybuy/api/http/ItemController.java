package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.dto.ItemPageQueryDTO;
import com.mizore.easybuy.model.entity.ItemAndImage;
import com.mizore.easybuy.model.vo.BasePageVO;
import com.mizore.easybuy.model.entity.TbItem;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.ItemInfo4SellerVO;
import com.mizore.easybuy.model.vo.PageResult;
import com.mizore.easybuy.service.base.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Slf4j
@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 商品分页查询
     * @param itemPageQueryDTO
     * @return
     */
    @RequestMapping("/page")
    public BaseVO<PageResult> page( ItemPageQueryDTO itemPageQueryDTO){
        log.info("商品分页查询：{}",itemPageQueryDTO);
        PageResult pageResult = itemService.page(itemPageQueryDTO);
        return new BaseVO<PageResult>().success(pageResult);
    }

    /**
     * 根据商品id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseVO<ItemAndImage> ItemDetails(@PathVariable Long id){
        log.info("根据商品id查询，id值：{}",id);
        ItemAndImage itemAndImage = itemService.getById(id);
        return new BaseVO<ItemAndImage>().success(itemAndImage);
    }

    /**
     * 上架商品
     * @param tbItem 商品基本信息
     * @param images 商品图片信息
     */
    @PostMapping
    public BaseVO<Object> addItem(
            @RequestBody TbItem tbItem,
            @RequestParam List<String> images
    ) {
        return itemService.addItem(tbItem, images);
    }

    /**
     * 根据商品id，下架指定商品
     * @param id 商品id
     * @throws RuntimeException
     */
    @DeleteMapping("/{id}")
    public BaseVO<Object> deleteItem(@PathVariable(value = "id") Integer id) throws Exception {
        return itemService.deleteItem(id);
    }

    @PostMapping("/status/{status}")
    public BaseVO UpdateItemStatus(@PathVariable Integer status,Integer id){
        log.info("根据商品id修改商品上下架状态，status值为：{},id值为：{}，",status,id);
        itemService.UpdateItemStatus(status,id);
        return new BaseVO().success();
    }

    /**
     * 商家根据条件查询商品信息，没有传的条件不做过滤
     * @param categoryId 分类id
     * @param statuses 按商品状态 可以传多个 查多个状态的
     * @param keyword 商品名称（模糊查询）
     * @param pageSize 页大小
     * @param pageNum 页码
     */
    @GetMapping("/management")
    public BasePageVO<List<ItemInfo4SellerVO>> sellerGetItems(
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "statuses", required = false) List<Integer> statuses,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum
            ) {
        return itemService.sellerGetItems(categoryId, statuses, keyword, pageSize, pageNum);
    }

}
