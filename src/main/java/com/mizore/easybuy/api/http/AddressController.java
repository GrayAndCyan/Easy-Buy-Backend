package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.entity.TbAddress;
import com.mizore.easybuy.model.vo.BasePageVO;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.base.ITbAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 地址表 前端控制器
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private ITbAddressService tbAddressService;

    /*
    @GetMapping("/search")
    public ResponseEntity<List<TbAddress>> searchAddress(@RequestParam("userId") Integer userId) {
        List<TbAddress> addresses = tbAddressService.searchadd(userId);
        if (addresses != null && !addresses.isEmpty()) {
            return ResponseEntity.ok(addresses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @GetMapping("/search")
    public ResponseEntity<List<TbAddress>> searchAddress() {
        List<TbAddress> addresses = tbAddressService.searchadd();
        if (addresses != null && !addresses.isEmpty()) {
            return ResponseEntity.ok(addresses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete")
    public BaseVO<Object> deleteAddr(
            @RequestParam(value="addressId") Integer addressId
    ) {
        BaseVO<Object> baseVO = new BaseVO<>().success();
        tbAddressService.deleteAddress(addressId);
        return baseVO;
    }

    @PostMapping("/delete")
    public int deleteAddress(@RequestParam("addressId") Integer addressId) {
        return tbAddressService.deleteAddress(addressId);
    }



}
