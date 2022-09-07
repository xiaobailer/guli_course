package com.atguigu.orderservice.controller;


import com.atguigu.baserservice.handler.GuliException;
import com.atguigu.commonutils.R;
import com.atguigu.orderservice.service.TPayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-09-05
 */
@Api(description="支付管理")
@RestController
//@CrossOrigin
@RequestMapping("/orderservice/t-pay-log")
public class TPayLogController {

    @Autowired
    private TPayLogService payLogService;

    @ApiOperation(value = "根据订单编号生成支付二维码")
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        Map<String,Object> map= payLogService.createNative(orderNo);
        return R.ok().data(map);
    }


    @ApiOperation(value = "根据订单编号查询支付状态")
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        //1.调用微信接口查询支付状态
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        //2.判断支付状态
        if (map == null) {
            return R.error().message("支付失败");
        }
        if ("SUCCESS".equals(map.get("trade_state"))) {
            //3支付成功后，更新订单信息，记录支付日志
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }

}

