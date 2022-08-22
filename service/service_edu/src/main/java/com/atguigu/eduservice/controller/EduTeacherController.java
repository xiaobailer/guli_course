package com.atguigu.eduservice.controller;


import com.atguigu.baserservice.handler.GuliException;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin(allowCredentials = "true") //解决跨域问题
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "获取教师所有列表")
    @GetMapping
    public R getAllTeacher() {
        List<EduTeacher> list = eduTeacherService.list(null);
        try {
            int i = 10 / 0;
        } catch (Exception e) {
            throw new GuliException(20001, "自定义异常");
        }
        return R.ok().data("list", list);
    }

    @ApiOperation(value = "删除教师")
    @DeleteMapping("{id}")
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
        IPage<EduTeacher> page = eduTeacherService.page(eduTeacherPage, null);
        long total = eduTeacherPage.getTotal();
        List<EduTeacher> records = eduTeacherPage.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("list", records);
        map.put("total", total);
        return R.ok().data(map);
    }

    @ApiOperation(value = "待条件分页查询教师列表")
    @PostMapping("selectByPageVo/{current}/{limit}")
    public R selectByPageVo(@PathVariable Long current, @PathVariable Long limit, @RequestBody TeacherQuery teacherQuery) {
        //@RequesyBody 把json串转换成实体类
        //1.取出查询条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //2.判断条件是否为空，如果不空拼写sql
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (level != null) {
            queryWrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end);
        }
        Page<EduTeacher> eduTeacherPage = new Page<>(current, limit);
        eduTeacherService.page(eduTeacherPage, queryWrapper);
        long total = eduTeacherPage.getTotal();
        List<EduTeacher> records = eduTeacherPage.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("list", records);
        map.put("total", total);
        return R.ok().data(map);
    }

    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }

    }

    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("selectTeacherById/{id}")
    public R selectTeacherById(@PathVariable String id) {
        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok().data("eduTeacher", teacher);
    }

    @ApiOperation(value = "修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean b = eduTeacherService.updateById(eduTeacher);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

