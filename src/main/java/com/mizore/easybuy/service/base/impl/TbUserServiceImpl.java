package com.mizore.easybuy.service.base.impl;

import com.google.common.collect.Maps;
import com.mizore.easybuy.mapper.TbSellerMapper;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbSeller;
import com.mizore.easybuy.model.entity.TbUser;
import com.mizore.easybuy.mapper.TbUserMapper;
import com.mizore.easybuy.model.enums.Result_login;
import com.mizore.easybuy.model.enums.Result_register;
import com.mizore.easybuy.model.enums.ReturnEnum;
import com.mizore.easybuy.model.enums.RoleEnum;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.base.ITbUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.easybuy.utils.JWTUtil;
import com.mizore.easybuy.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {

    @Autowired
    private TbSellerMapper tbSellerMapper;

    @Transactional
    public BaseVO openStore(String name, String address) {
        BaseVO<String> baseVO = new BaseVO<>();
        // 新增店铺（卖家）
        TbSeller tbSeller = new TbSeller();
        // 获取当前登录用户
        UserDTO userDTO = UserHolder.get();
        // 判断当前用户角色是否为普通买家，若是，则能够开店，赋予其卖家角色
        if(RoleEnum.BUYER.getCode().equals(userDTO.getRole())){
            // 设置卖家对应的用户账号id
            tbSeller.setUserId(userDTO.getId());
            // 设置店铺名称
            tbSeller.setName(name);
            // 设置店铺地址
            tbSeller.setAddress(address);
            // 将新增店铺信息插入数据库
            tbSellerMapper.insert(tbSeller);
            // 赋予用户卖家角色，更新数据库中用户信息
            update().set("role", RoleEnum.SELLER.getCode())
                    .eq("id",userDTO.getId())
                    .eq("role", RoleEnum.BUYER.getCode())
                    .update();
            return baseVO.success();
        } else {
            baseVO.setCode(ReturnEnum.FAILURE.getCode());
            baseVO.setMessage("Open store failed, role error!");
            return baseVO;
        }
    }


    @Autowired
    TbUserMapper userMapper;
    //返回注册结果
    // @Override
    public Result_register registerService(String username, String password)
    {
        //新建一个result实例
        Result_register result = new Result_register();
        //先调用mapper层方法，判断用户名是否已存在
        List<TbUser> list = userMapper.username_exist(username);
        //if list为空，即用户名不存在，可以进行下一步,即写数据库
        if(list.isEmpty()){
            //按照输入信息，创建一个新的user实例写入数据库
            TbUser user = new TbUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(1);
            user.setDeleted((byte) 0);
            boolean back =userMapper.register_user(user);
            //如果插入成功，则返回一个成功情况下的result
            if(back){
                result.status=0;
                result.description="succeed.";
            }
            //如果插入失败，则返回一个失败情况下的result
            else{
                result.status=1;
                result.description="register failed,please try again.";
            }
        }
        //else 用户名已存在，return一个失败的result
        else{
            result.status=1;
            result.description="username is already in use.";
        }
        return result;
    }

    //返回登录结果（List形式返回符合输入用户名密码的用户）
    // @Override
    public Result_login loginService (String username, String password) {



        List<TbUser> user = userMapper.login(username, password);
        Result_login result=new Result_login();
        //如果返回列表为空，则用户名或密码错误
        if(user.isEmpty()){
            result.id=0;
            result.role=0;
            result.status=1;
            result.description="please check your username and password.";
            result.token=" ";
        }
        //列表非空，则说明用户名、密码正确，返回的用户为需要的用户
        else{//生成token
            Map<String,Object> newClaims= Maps.newHashMap();
            newClaims.put("id",user.get(0).getId());
            newClaims.put("role",user.get(0).getRole());
            newClaims.put("username",user.get(0).getUsername());
            String newToken=JWTUtil.generateJWT(newClaims);

            result.id= user.get(0).getId();
            result.status=0;
            result.role=user.get(0).getRole();
            result.description="succeed";
            result.token=newToken;
        }
        return result;
    }

}
