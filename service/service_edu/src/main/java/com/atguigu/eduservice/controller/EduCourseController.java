package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-24
 */
@Api(description = "课程管理")
@RestController
@RequestMapping("/eduservice/edu-course")
@CrossOrigin // 解决跨域问题
public class EduCourseController {
    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation(value = "添加课程学习")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoForm courseInfoForm) {
        String courseId = eduCourseService.addCourseInfo(courseInfoForm);
        return R.ok().data("courseId",courseId);
    }

    @ApiOperation(value = "根据课程id查询数据")
    @GetMapping("getCourseInfoById/{id}")
    public R getCourseInfoById(@PathVariable("id") String id) {
        CourseInfoForm courseInfoForm = eduCourseService.getCourseInfoById(id);
        return R.ok().data("courseInfo", courseInfoForm);
    }

    @ApiOperation(value = "添加课程学习")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoForm courseInfoForm) {
        eduCourseService.updateCourseInfo(courseInfoForm);
        return R.ok();
    }
}

