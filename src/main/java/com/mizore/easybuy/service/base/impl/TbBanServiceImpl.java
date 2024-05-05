package com.mizore.easybuy.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mizore.easybuy.mapper.TbBanMapper;
import com.mizore.easybuy.model.entity.TbBan;
import com.mizore.easybuy.service.base.ITbBanService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
* @author mizore
* @description 针对表【tb_ban(封禁表)】的数据库操作Service实现
* @createDate 2024-05-05 12:22:32
*/
@Service
public class TbBanServiceImpl extends ServiceImpl<TbBanMapper, TbBan>
    implements ITbBanService {

    @Override
    public List<Integer> listBanningUserId() {
        LambdaQueryWrapper<TbBan> query = new LambdaQueryWrapper<TbBan>()
                .select(TbBan::getUserId)
                .gt(TbBan::getEnd, LocalDateTime.now());
        List<TbBan> users = list(query);
        if (CollectionUtil.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users.stream().map(TbBan::getUserId).toList();
    }

    @Override
    public boolean isBanning(int userId) {
        LambdaQueryWrapper<TbBan> query = new LambdaQueryWrapper<TbBan>()
                .eq(TbBan::getUserId, userId)
                .gt(TbBan::getEnd, LocalDateTime.now());
        return exists(query);
    }

    /**
     * @param duration 封禁时长，单位秒
     */
    @Override
    public void ban(int userId, long duration, String reason) {
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(duration);
        reason = StrUtil.isBlank(reason) ? StrUtil.EMPTY : reason;
        if (isBanning(userId)) {
            LambdaUpdateWrapper<TbBan> updateWrapper = new LambdaUpdateWrapper<TbBan>()
                    .set(TbBan::getEnd, endTime)
                    .eq(TbBan::getUserId, userId);
            update(updateWrapper);
        } else {
            TbBan toSave = TbBan.builder()
                    .start(LocalDateTime.now())
                    .end(endTime)
                    .userId(userId)
                    .reason(reason)
                    .build();
            save(toSave);
        }
    }
}




