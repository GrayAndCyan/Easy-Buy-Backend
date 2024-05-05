package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.entity.TbBan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author mizore
* @description 针对表【tb_ban(封禁表)】的数据库操作Service
* @createDate 2024-05-05 12:22:32
*/
public interface ITbBanService extends IService<TbBan> {

    List<Integer> listBanningUserId();

    boolean isBanning(int userId);

    void ban(int userId, long duration, String reason);
}
