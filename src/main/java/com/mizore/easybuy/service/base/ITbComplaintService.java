package com.mizore.easybuy.service.base;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mizore.easybuy.model.entity.TbComplaint;

import java.util.List;

/**
* @author mizore
* @description 针对表【tb_complaint(投诉表)】的数据库操作Service
* @createDate 2024-05-04 14:01:05
*/
public interface ITbComplaintService extends IService<TbComplaint> {

    List<TbComplaint> search(Integer status);
}
