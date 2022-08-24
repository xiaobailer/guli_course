package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.OneSubjectVo;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-23
 */
@Api(description = "课程分类管理")
@RestController
@RequestMapping("/eduservice/edu-subject")
@CrossOrigin // 解决跨域问题
public class EduSubjectController {
    @Autowired
    private EduSubjectService eduSubjectService;

    @ApiOperation(value = "批量导入课程")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file) {
        //手动传入service
        eduSubjectService.addSubject(file,eduSubjectService);
        return R.ok();
    }


    @ApiOperation(value = "查询所有讲师")
    @GetMapping("getAllSubject")
    public R getAllSubject() {
        List<OneSubjectVo> allSubject =  eduSubjectService.getAllSubject();
        return R.ok().data("allSubject", allSubject);
    }
}

