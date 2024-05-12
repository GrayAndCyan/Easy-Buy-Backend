package com.mizore.easybuy.mapper;

import com.github.pagehelper.Page;
import com.mizore.easybuy.model.dto.ItemPageQueryDTO;
import com.mizore.easybuy.model.entity.ItemAndImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
     * 根据商品id上架商品
     * @param itemAndImage
     */
    void onshelf(ItemAndImage itemAndImage);

    /**
     * 根据商品id下架商品
     * @param itemAndImage
     */
    void offshelf(ItemAndImage itemAndImage);

    /**
     * 根据卖家id删除商品
     * @param id
     */
    @Update("update tb_item set deleted = 1 where seller_id = #{id} ")
    void deleteItem(Integer id);

    /**
     * 删除商品图片
     */
    @Update("update tb_item_image set deleted = 1 where item_id in " +
            "(select id from tb_item where deleted = 1)")
    void deleteItemImage();
}
