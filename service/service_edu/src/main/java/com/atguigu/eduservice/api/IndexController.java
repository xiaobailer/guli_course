package com.atguigu.eduservice.api;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/index")
@Api(description = "首页显示")
@CrossOrigin //跨域
public class IndexController {
    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "首页展示8条课程信息，4讲师信息")
    @GetMapping("getCourseTeacherList")
    public R getCourseTeacherList() {
        //8条课程信息
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("gmt_create");
        courseWrapper.last("limit 8");
        List<EduCourse> eduCourses = eduCourseService.list(courseWrapper);

        //4条讲师信息
        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByDesc("gmt_create");
        teacherWrapper.last("limit 4");
        List<EduTeacher> eduTeachers = eduTeacherService.list(teacherWrapper);
        return R.ok().data("eduCourse", eduCourses).data("eduTeachers", eduTeachers);
    }
}
