package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation(value = "根据课程id查询课程信息")
    @GetMapping("getCursePublicById/{id}")
    public R getCursePublicById(@PathVariable String id) {
        CoursePublishVo coursePublishVo = eduCourseService.getCursePublicById(id);
        return R.ok().data("coursePublishVo", coursePublishVo);
    }

    @ApiOperation(value = "根据id更改发布状态")
    @PutMapping("updateStatusById/{id}")
    public R updateStatusById(@PathVariable String id) {
        //修改前是需要先查询数据
        EduCourse eduCourse = eduCourseService.getById(id);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    @ApiOperation(value = "查询所有课程信息")
    @GetMapping("getCourseInfo")
    public R getCourseInfo() {
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list",list);
    }
    //TODO 实现带条件，带分页查询 ，跟查询讲师列表差不多

}

