package com.atguigu.smsservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.smsservice.service.SmsService;
import com.atguigu.smsservice.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(description = "短信管理")
@RestController
@RequestMapping("edusms/sms")
//@CrossOrigin //跨域
public class SmsController {
    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @ApiOperation(value = "发送短信")
    @GetMapping("sentMessage/{phone}") //手机号传入后端，get是从后端获取数据
    public R sentMessage(@PathVariable("phone") String phone) {
        //1.拿到手机号到redis查询验证码(注入 RedisTemplate)
        String rPhone = redisTemplate.opsForValue().get(phone);
        //2.验证验证码是否存在，存在，直接返回
        if ( rPhone != null) {
            return R.ok();
        }
        //3.验证码不存在，生成验证码
        String code = RandomUtil.getFourBitRandom();
        //4.调用接口服务发送短信
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        Boolean isSend = smsService.sentMessage(rPhone, map);
        //5.发送成功，验证码存入redis,时效五分钟
        if (isSend) {
            redisTemplate.opsForValue().set(rPhone, code,5, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error();
        }

    }
}
