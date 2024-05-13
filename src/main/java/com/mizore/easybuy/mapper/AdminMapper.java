package com.mizore.easybuy.mapper;

import com.github.pagehelper.Page;
import com.mizore.easybuy.model.dto.ReturnUserDto;
import com.mizore.easybuy.model.entity.TbAddress;
import com.mizore.easybuy.model.query.PageQuery;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AdminMapper {

    @Select("select u.id,u.username,a.addr_phone,u.role from tb_user u left join tb_address a on a.user_id = u.id where u.deleted = 0")
    Page<ReturnUserDto> userList(PageQuery pageQuery);

    @Delete("delete from tb_seller where id = #{id}")
    void sellerRemove(Integer id);

    @Update("update tb_user set role = 1 where id = #{id}")
    void userRemove(Integer id);

    @Select("select * from tb_ban where user_id = #{id}")
    TbAddress getUserStatus(ReturnUserDto returnUserDto);

    @Select("select user_id from tb_seller where id = #{id}")
    int getUserIdById(Integer id);
}
