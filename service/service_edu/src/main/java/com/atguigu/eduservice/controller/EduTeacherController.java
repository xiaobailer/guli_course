package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-15
 */

@Api(description = "教师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "获取教师所有列表")
    @GetMapping
    public R getAllTeacher() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("list", list);
    }

    @ApiOperation(value = "删除教师")
    @GetMapping("{id}")
    public R delTeacher(@PathVariable String id) {
        boolean remove = eduTeacherService.removeById(id);
        if (remove) {
            return R.ok();
        } else {
            return R.error();
        }

    }

    @ApiOperation(value = "分页查询教师列表")
    @GetMapping("selectByPage/{current}/{limit}")
    public R selectByPage(@PathVariable Long current, @PathVariable Long limit) {
        Page<EduTeacher> eduTeacherPage = new Page<>(current, limit);
        long total = eduTeacherPage.getTotal();
        List<EduTeacher> records = eduTeacherPage.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("list", records);
        map.put("total", total);
        return R.ok().data(map);
    }
}

