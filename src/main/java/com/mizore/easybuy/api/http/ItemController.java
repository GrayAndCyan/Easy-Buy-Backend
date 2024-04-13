package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.dto.ItemPageQueryDTO;
import com.mizore.easybuy.model.entity.ItemAndImage;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.PageResult;
import com.mizore.easybuy.service.base.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public BaseVO<PageResult> page(@RequestBody ItemPageQueryDTO itemPageQueryDTO){
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

}
