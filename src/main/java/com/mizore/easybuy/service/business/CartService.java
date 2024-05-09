package com.mizore.easybuy.service.business;

import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbCart;
import com.mizore.easybuy.model.enums.ReturnEnum;
import com.mizore.easybuy.model.query.CartAddQuery;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.base.ITbCartService;
import com.mizore.easybuy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartService {

    @Autowired
    private ITbCartService tbCartService;

    public BaseVO<Object> addToCart(CartAddQuery cartAddQuery) {
        BaseVO<Object> baseVO = new BaseVO<>();
        boolean res;
        // 当前登录用户
        UserDTO user = UserHolder.get();
        // 获取用户id
        Integer userId = user.getId();
        // 获取商品id
        Integer itemId = cartAddQuery.getItemId();
        // 获取当前用户购物车中是否已经有该商品记录
        TbCart tbCart = tbCartService.itemExist(userId, itemId);
        // 若存在，则更新quantity
        if(tbCart != null) {
            Integer quantity = cartAddQuery.getQuantity() + tbCart.getQuantity();
            // 在数据库更新购物车中商品数量
            tbCart.setQuantity(quantity);
            res = tbCartService.updateById(tbCart);
        } else {    // 不存在则插入一条购物车信息
            // 创建一条购物车信息
            TbCart newCart = TbCart.builder()
                    .userId(userId)
                    .itemId(itemId)
                    .quantity(cartAddQuery.getQuantity())
                    .build();
            // 将购物车信息插入数据库
            res = tbCartService.save(newCart);
        }

        return res ? baseVO.success()
                : baseVO.setCode(ReturnEnum.FAILURE.getCode()).setMessage("operation failed");
    }

}
