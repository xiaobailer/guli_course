package com.atguigu.eduservice.entity.vo;

import lombok.Data;

@Data
public class CoursePublishVo {
    private String id;
    private String title;
    private String cover;  //课程封面
    private Integer lessonNum;
    private String subjectLevelOne; //一级目录
    private String subjectLevelTwo; //二级目录
    private String teacherName; //教师名称
    private String price;//只用于显示
}
