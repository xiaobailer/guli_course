package com.atguigu.smsservice.service;

import java.util.Map;

public interface SmsService {

    Boolean sentMessage(String rPhone, Map<String, String> map);
}
