package com.atguigu.smsservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.smsservice.service.SmsService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SmsserviceImpl implements SmsService {

    @Override
    public Boolean sentMessage(String rPhone, Map<String, String> param) {
        if(StringUtils.isEmpty(rPhone)) return false;

        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI5tEeM6EFMnRZnigakmp9", "Mh8HwPhuoEoLFP8WBFtLPKHkLT3wAs");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", rPhone);
        request.putQueryParameter("SignName", "我的谷粒在线教育网站");//审核通过签名,这个要跟阿里云中的模板要一致，不然也调用不到这个服务
        request.putQueryParameter("TemplateCode", "SMS_183195440");//审核通过模板
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
