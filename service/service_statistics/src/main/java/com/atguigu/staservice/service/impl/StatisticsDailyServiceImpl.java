package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-06
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    //生成统计数据
    @Override
    public void createStaDaily(String day) {
        //1删除数据
        QueryWrapper<StatisticsDaily> queryWrapperdel = new QueryWrapper<>();
        queryWrapperdel.eq("date_calculated",day);
        baseMapper.delete(queryWrapperdel);
        //2统计数据
        R r = ucenterClient.countRegister(day);
        Integer registerNum = (Integer)r.getData().get("count");
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO
        //3封装数据，入库
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);

    }

    @Override
    public Map<String, Object> getStaDaily(String type, String begin, String end) {
        //1.查询数据
        QueryWrapper<StatisticsDaily> staWrapper = new QueryWrapper<>();
        staWrapper.between("date_calculated", begin, end);
        //1.1查询相关字段
        staWrapper.select("date_calculated", type);
        //2.遍历查询结果
        List<StatisticsDaily> dailieList = baseMapper.selectList(staWrapper);
        //2 遍历查询结果
        Map<String, Object> staDailyMap = new HashMap<>();
        List<String> dateCalculatedList = new ArrayList<>();
        List<Integer> dataList =  new ArrayList<>();

        for (int i = 0; i < dailieList.size(); i++) {
            StatisticsDaily daily = dailieList.get(i);
            //3 封装X轴数据
            dateCalculatedList.add(daily.getDateCalculated());
            //4 封装Y轴数据
            switch (type){
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        staDailyMap.put("dateCalculatedList",dateCalculatedList);
        staDailyMap.put("dataList",dataList);
        return staDailyMap;
    }
    }
