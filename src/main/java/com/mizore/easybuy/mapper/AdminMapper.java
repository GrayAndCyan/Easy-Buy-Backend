package com.mizore.easybuy.mapper;

import com.github.pagehelper.Page;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.query.PageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AdminMapper {

    @Select("select id,username,role from tb_user")
    Page<UserDTO> userList(PageQuery pageQuery);

    @Update("update tb_seller set deleted = 1 where id = #{id}")
    void sellerRemove(Integer id);

    @Update("update tb_user set deleted = 1 where id = #{id}")
    void userRemove(Integer id);

    @Select("select user_id from tb_seller where id = #{id}")
    int getUserIdById(Integer id);
}
