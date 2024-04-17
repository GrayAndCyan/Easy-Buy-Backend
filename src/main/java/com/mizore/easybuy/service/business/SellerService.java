package com.mizore.easybuy.service.business;

import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbSeller;
import com.mizore.easybuy.model.entity.TbUser;
import com.mizore.easybuy.model.enums.RoleEnum;
import com.mizore.easybuy.model.query.SellerUpdateQuery;
import com.mizore.easybuy.service.base.ITbSellerService;
import com.mizore.easybuy.service.base.ITbUserService;
import com.mizore.easybuy.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellerService {

    @Autowired
    private ITbSellerService tbSellerService;

    @Autowired
    private ITbUserService tbUserService;

    public void update(SellerUpdateQuery sellerUpdateQuery) {
        // todo 先不校验身份与店铺对应了
        Integer sellerId = sellerUpdateQuery.getSellerId();
        if (sellerId != null) {
            TbSeller tbSeller = new TbSeller();
            tbSeller.setAddress(sellerUpdateQuery.getAddress());
            tbSeller.setId(sellerId);
            tbSellerService.updateById(tbSeller);
        }
    }

    @Transactional
    public void del(Integer sellerId) throws Exception {
        tbSellerService.removeById(sellerId);
        UserDTO userDTO = UserHolder.get();
        if (userDTO != null) {
            TbUser tbUser = new TbUser();
            tbUser.setId(userDTO.getId());
            tbUser.setRole(RoleEnum.BUYER.getCode());
            tbUserService.updateById(tbUser);
        } else {
            throw new Exception();
        }
    }
}
