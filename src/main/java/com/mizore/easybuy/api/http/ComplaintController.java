package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.query.ComplaintSaveQuery;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.ComplaintSearchVO;
import com.mizore.easybuy.service.business.ComplaintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("complaint")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    // 保存投诉单    - 根据token确认发起人和类型
    @PostMapping("/save")
    public BaseVO<Object> saveComplaint(
            @RequestBody ComplaintSaveQuery complaintSaveQuery
            ) {
        return complaintService.saveComplaint(complaintSaveQuery);
    }

    // admin：查
    @GetMapping("search")
    public BaseVO<List<ComplaintSearchVO>> search(
            @RequestParam(required = false) Integer status
    ) {
        log.info("投诉信息查询：");
        return complaintService.search(status);
    }

    // admin：已处理指定投诉单
    @GetMapping("processed")
    public BaseVO<Object> processed(@RequestParam Integer complaintId) {
        return complaintService.processed(complaintId);
    }

}
