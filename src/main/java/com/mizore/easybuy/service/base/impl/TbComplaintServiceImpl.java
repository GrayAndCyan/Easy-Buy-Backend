package com.mizore.easybuy.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.easybuy.mapper.TbComplaintMapper;
import com.mizore.easybuy.model.entity.TbComplaint;
import com.mizore.easybuy.service.base.ITbComplaintService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mizore
* @description 针对表【tb_complaint(投诉表)】的数据库操作Service实现
* @createDate 2024-05-04 14:01:05
*/
@Service
public class TbComplaintServiceImpl extends ServiceImpl<TbComplaintMapper, TbComplaint>
    implements ITbComplaintService {

    @Override
    public List<TbComplaint> search(Integer status) {
        LambdaQueryWrapper<TbComplaint> query = new LambdaQueryWrapper<>();
        if (status != null && status != 0) {
            query.eq(TbComplaint::getStatus, status);
        }
        return list(query);
    }
}




