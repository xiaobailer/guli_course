package com.atguigu.staservice.scheduled;


import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService dailyService;
    //    @Scheduled(cron = "0/5 * * * * ?")
    //    public void task1(){
    //        System.out.println("task1-----------------------------");
    //    }
    //每天凌晨1点跑前一天数据
    @Scheduled(cron = "0 0 1 * * ? ") //七子表达式
    public void task2(){
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(),-1)); //生成前一天的数据
        dailyService.createStaDaily(day);
        System.out.println("生成数据成功"+day);
    }

}
