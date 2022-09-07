package com.atguigu.orderservice.service.impl;

import com.atguigu.baserservice.handler.GuliException;
import com.atguigu.commonutils.vo.CourseWebVoForOrder;
import com.atguigu.commonutils.vo.UcenterMemberForOrder;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-05
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {


    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;
    //根据课程id，用户id创建订单
    @Override
    public String createOrder(String courseId, String memberId) {
        //1.跨模块获取课程信息
        CourseWebVoForOrder courseInfoForOrder = eduClient.getCourseInfoForOrder(courseId);
        //1.1.对传回的值进行校验
        if (courseInfoForOrder == null) {
            throw  new GuliException(20001,"获取课程信息失败");
        }
        //2.跨模块获取用户信息
        UcenterMemberForOrder ucenterForOrder = ucenterClient.getUcenterForOrder(memberId);
        if (ucenterForOrder == null) {
            throw new GuliException(20001, "获取用户信息失败");
        }

        //3.生成订单编号
        String orderNo = OrderNoUtil.getOrderNo();
        //4.封装数据，存入数据库
        TOrder order = new TOrder();
        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoForOrder.getTitle());
        order.setCourseCover(courseInfoForOrder.getCover());
        order.setTeacherName(courseInfoForOrder.getTeacherName());
        order.setTotalFee(courseInfoForOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterForOrder.getMobile());
        order.setNickname(ucenterForOrder.getNickname());
        order.setStatus(0);//未支付
        order.setPayType(1);//1：微信
        baseMapper.insert(order);

        return orderNo;
    }
}
