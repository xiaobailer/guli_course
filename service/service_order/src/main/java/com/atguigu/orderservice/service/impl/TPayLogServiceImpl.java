package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.baserservice.handler.GuliException;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.entity.TPayLog;
import com.atguigu.orderservice.mapper.TPayLogMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.service.TPayLogService;
import com.atguigu.orderservice.utils.HttpClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-05
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    private TOrderService orderService;

    @Override
    public Map<String, Object> createNative(String orderNo){
        try {
            //查询订单信息
            QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_no", orderNo);
            TOrder order = orderService.getOne(queryWrapper);

            if (order == null) {
                throw new GuliException(20001, "订单失效");
            }
            //2封装支付参数
            Map m = new HashMap();
            //设置支付参数
            m.put("appid", "wx74862e0dfcf69954");//应用id
            m.put("mch_id", "1558950191");//商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
            m.put("body", order.getCourseTitle());//课程名称
            m.put("out_trade_no", orderNo);//订单编号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");//交易金额
            m.put("spbill_create_ip", "127.0.0.1");//终端ip
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");//回调地址
            m.put("trade_type", "NATIVE");//交易类型

            //3通过httpclient发送请求,参数转化成xml
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //4 获取返回值
            String xml = client.getContent();
            System.out.println("xml="+xml);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //5、封装返回结果集

            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"获取二维码失败");
        }

    }

    //调用微信接口查询支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {

        try {
            //1封装支付参数
            Map m = new HashMap();
            //设置支付参数
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            System.out.println("订单状态="+xml);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"查询订单状态失败");
        }

    }

    //支付成功后，更新订单支付状态，记录支付日志
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //1.更新订单状态
        //1.1获取订单编号
        String orderNo = map.get("out_trade_no");
        //1.2查询订单信息
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        TOrder order = orderService.getOne(queryWrapper);
        //1.3更新状态
        if(order.getStatus().intValue() == 1) return;
        order.setStatus(1);
        orderService.updateById(order);

        //2记录支付日志
        TPayLog payLog = new TPayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表

    }

}
