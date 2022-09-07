package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-09-05
 */
@Api(description="前台课程展示")
@RestController
@RequestMapping("/orderservice/t-order")
//@CrossOrigin //跨域
public class TOrderController {
    @Autowired
    private TOrderService orderService;
    //生成订单
    @ApiOperation(value = "根据课程id和用户id创建订单信息")
    @GetMapping("/createrOrder/{courseId}")
    public R createrOrder(@PathVariable String courseId, HttpServletRequest request) {
        //从tokenh'中获取用户信息
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println(memberId);
        String orderNo = orderService.createOrder(courseId,memberId);
        //返回订单id订单号order_
        return R.ok().data("orderNo",orderNo);
    }

    //根据订单id查询订单信息
    @ApiOperation(value = "根据订单编号查询订单信息")
    @GetMapping("getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo) {
        QueryWrapper<TOrder> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("order_no", orderNo);
        TOrder order = orderService.getOne(objectQueryWrapper);
        return R.ok().data("order", order);
    }

    //查询订单课程id，用户id是否已购买课程
    @ApiOperation(value = "查询订单课程id，用户id是否已购买课程【跨模块调用】")
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId,
                               @PathVariable("memberId") String memberId
    ){
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        int count = orderService.count(queryWrapper);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

}

