package com.atguigu.eduservice.api;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description="前台讲师展示")
@RestController
@RequestMapping("/eduservice/teacherapi")
@CrossOrigin
public class TeacherApiController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "前台分页查询讲师列表")
    @GetMapping("getTeacherApiPage/{current}/{limit}")
    public R getTeacherApiPage(@PathVariable Long current,
                               @PathVariable Long limit) {
        Page<EduTeacher> page = new Page<>(current, limit);
        Map<String, Object> map = teacherService.getTeacherApiPage(page);
        return R.ok().data(map);
    }

    @ApiOperation(value = "前台查询讲师详情")
    @GetMapping("getTeacherCourseById/{teacherId}")
    public R getTeacherCourseById(@PathVariable String teacherId) {
        //1讲师信息
        EduTeacher eduTeacher = teacherService.getById(teacherId);
        //2相关课程
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacherId);
        List<EduCourse> courseList = courseService.list(queryWrapper);
        return R.ok().data("eduTeacher", eduTeacher).data("courseList", courseList);
    }
}

