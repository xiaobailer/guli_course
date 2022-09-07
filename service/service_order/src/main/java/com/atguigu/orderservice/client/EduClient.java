package com.atguigu.orderservice.client;

import com.atguigu.commonutils.vo.CourseWebVoForOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//跨模块调用远程接口
@Component
@FeignClient("service-edu")
public interface EduClient {
    //跨模块远程调用获取课程信息
    @GetMapping("/eduservice/courseapi/getCourseInfoForOrder/{courseId}")
    public CourseWebVoForOrder getCourseInfoForOrder(
            @PathVariable("courseId") String courseId);
}
