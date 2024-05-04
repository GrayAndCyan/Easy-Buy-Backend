package com.mizore.easybuy.service.business;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbComplaint;
import com.mizore.easybuy.model.entity.TbOrder;
import com.mizore.easybuy.model.entity.TbSeller;
import com.mizore.easybuy.model.entity.TbUser;
import com.mizore.easybuy.model.enums.ComplaintStatusEnum;
import com.mizore.easybuy.model.enums.ComplaintTypeEnum;
import com.mizore.easybuy.model.enums.RoleEnum;
import com.mizore.easybuy.model.query.ComplaintSaveQuery;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.ComplaintSearchVO;
import com.mizore.easybuy.service.base.ITbComplaintService;
import com.mizore.easybuy.service.base.ITbOrderService;
import com.mizore.easybuy.service.base.ITbSellerService;
import com.mizore.easybuy.service.base.ITbUserService;
import com.mizore.easybuy.utils.UserHolder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

@Service
public class ComplaintService {

    @Autowired
    private ITbComplaintService complaintService;

    @Autowired
    private ITbUserService userService;

    @Autowired
    private ITbOrderService orderService;

    @Autowired
    private ITbSellerService sellerService;

    private Map<Integer, BiConsumer<TbComplaint, ComplaintSearchVO>> complaintVOBuilders;

    @PostConstruct
    public void init() {
        complaintVOBuilders = Maps.newHashMap();
        complaintVOBuilders.put(ComplaintTypeEnum.BUYER_COMPLAINT.getCode(), this::buildBuyerComplaintVO);
        complaintVOBuilders.put(ComplaintTypeEnum.SELLER_COMPLAINT.getCode(), this::buildSellerComplaintVO);
    }


    public BaseVO<Object> saveComplaint(ComplaintSaveQuery complaintSaveQuery) {
        BaseVO<Object> baseVO = new BaseVO<>();

        if (complaintSaveQuery == null || StrUtil.isBlank(complaintSaveQuery.getReason())
                || complaintSaveQuery.getOrderId() == 0) {
            return baseVO.failure().setMessage("参数有误！！");
        }
        UserDTO loginUser = UserHolder.get();
        if (loginUser == null) {
            return baseVO.failure().setMessage("未登陆");
        }

        // get type
        int complaintTypeCode = 0;
        int role = loginUser.getRole();
        if (Objects.equals(role, RoleEnum.BUYER.getCode())) {
            // buyer
            complaintTypeCode = ComplaintTypeEnum.BUYER_COMPLAINT.getCode();
        } else if (Objects.equals(role, RoleEnum.SELLER.getCode())) {
            complaintTypeCode = ComplaintTypeEnum.SELLER_COMPLAINT.getCode();
        }


        TbComplaint toSave = TbComplaint.builder()
                .orderId(complaintSaveQuery.getOrderId())
                .reason(complaintSaveQuery.getReason())
                .userId(loginUser.getId())
                .status(ComplaintStatusEnum.PROCESSING.getCode())
                .type(complaintTypeCode)
                .build();
        boolean res = complaintService.save(toSave);
        return res ? baseVO.success() : baseVO.failure().setMessage("保存失败！！");
    }

    public BaseVO<List<ComplaintSearchVO>> search(
            Integer status
    ) {
        BaseVO<List<ComplaintSearchVO>> baseVO = new BaseVO<>();
        List<TbComplaint> complaints = complaintService.search(status);
        List<ComplaintSearchVO> res = Lists.newArrayList();
        if (CollectionUtil.isEmpty(complaints)) {
            return baseVO.success();
        }

        complaints.forEach(complaint -> res.add(model2VO(complaint)));

        // 优先展示处理中，其次创建时间倒序
        res.sort((o1, o2) -> Objects.equals(o1.getStatusCode(), o2.getStatusCode()) ?
                (o2.getCtime().after(o1.getCtime()) ? 1 : 0) :
                o1.getStatusCode() - o2.getStatusCode());
        return baseVO.success(res);
    }


    private ComplaintSearchVO model2VO(TbComplaint tbComplaint) {
        if (tbComplaint == null) {
            return new ComplaintSearchVO();
        }
        Integer orderId = tbComplaint.getOrderId();
        String reason = tbComplaint.getReason();
        Integer status = tbComplaint.getStatus();
        Integer type = tbComplaint.getType();
        ComplaintSearchVO complaintSearchVO = ComplaintSearchVO.builder()
                .complaintId(tbComplaint.getId())
                .ctime(tbComplaint.getCtime())
                .orderId(orderId)
                .reason(StrUtil.isBlank(reason) ? StrUtil.EMPTY : reason)
                .statusCode(status)
                .statusDesc(ComplaintStatusEnum.getByCode(status).getDesc())
                .typeCode(type)
                .typeDesc(ComplaintTypeEnum.getByCode(type).getDesc())
                .build();

        // 填充主客体
        complaintVOBuilders.getOrDefault(type, this::buildDefaultComplaintVO).accept(tbComplaint, complaintSearchVO);

        return complaintSearchVO;
    }

    private void buildDefaultComplaintVO(TbComplaint tbComplaint, ComplaintSearchVO complaintSearchVO) {
        String defaultName =StrUtil.EMPTY;
        complaintSearchVO.setComplainantName(defaultName);
        complaintSearchVO.setRespondentName(defaultName);
    }

    public BaseVO<Object> processed(Integer complaintId) {
        BaseVO<Object> baseVO = new BaseVO<>();
        if (complaintId == null) {
            return baseVO.failure().setMessage("参数有误！！");
        }
        TbComplaint complaint = complaintService.getById(complaintId);
        if (complaint == null) {
            return baseVO.failure().setMessage("不存在该投诉单");
        }

        complaint.setStatus(ComplaintStatusEnum.FINISHED.getCode());
        boolean res = complaintService.updateById(complaint);
        return res ? baseVO.success() : baseVO.failure().setMessage("保存更改失败");
    }

    private void buildSellerComplaintVO(TbComplaint tbComplaint, ComplaintSearchVO complaintSearchVO) {
        String complainantName = StrUtil.EMPTY;
        Integer complainantId = 0;
        String respondentName = StrUtil.EMPTY;
        Integer respondentId = 0;
        // 根据orderId 查seller, consumer
        TbOrder order = orderService.getById(tbComplaint.getOrderId());
        if (order != null) {
            Integer sellerId = order.getSellerId();
            complainantId = sellerId;
            TbSeller seller = sellerService.getById(sellerId);
            complainantName = seller.getName();

            Integer userId = order.getUserId();
            respondentId = userId;
            TbUser user = userService.getById(userId);
            if (user != null) {
                respondentName = user.getUsername();
            }
        }

        complaintSearchVO.setComplainantId(complainantId);
        complaintSearchVO.setComplainantName(complainantName);
        complaintSearchVO.setRespondentId(respondentId);
        complaintSearchVO.setRespondentName(respondentName);
    }

    private void buildBuyerComplaintVO(TbComplaint tbComplaint, ComplaintSearchVO complaintSearchVO) {
        String complainantName = StrUtil.EMPTY;
        Integer complainantId;
        String respondentName = StrUtil.EMPTY;
        Integer respondentId = 0;
        // buyer
        Integer userId = tbComplaint.getUserId();
        complainantId = userId;
        TbUser user = userService.getById(userId);
        if (user != null) {
            complainantName = user.getUsername();
        }

        // 根据orderId 查seller
        TbOrder order = orderService.getById(tbComplaint.getOrderId());
        if (order != null) {
            Integer sellerId = order.getSellerId();
            respondentId = sellerId;
            TbSeller seller = sellerService.getById(sellerId);
            respondentName = seller.getName();
        }

        complaintSearchVO.setComplainantId(complainantId);
        complaintSearchVO.setComplainantName(complainantName);
        complaintSearchVO.setRespondentId(respondentId);
        complaintSearchVO.setRespondentName(respondentName);
    }

}
