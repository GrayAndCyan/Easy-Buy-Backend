package com.mizore.easybuy.mapper;

import com.github.pagehelper.Page;
import com.mizore.easybuy.model.dto.ItemPageQueryDTO;
import com.mizore.easybuy.model.entity.ItemAndImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ItemMapper {

    /**
     * 商品分页查询
     * @param itemPageQueryDTO
     * @return
     */
    Page<ItemAndImage> page(ItemPageQueryDTO itemPageQueryDTO);

    /**
     * 根据商品id查询
     * @param id
     * @return
     */
    @Select("select a.*,b.url from tb_item a left join tb_item_image b on a.id = b.item_id " +
            "where a.id = #{id}")
    ItemAndImage getById(Long id);

    /**
     * 根据商品id修改商品status
     * @param itemAndImage
     */
    void UpdateItemStatus(ItemAndImage itemAndImage);
}
