package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-26
 */
@RestController
@RequestMapping("/eduservice/edu-chapter")
@Api(description = "章节管理")
@CrossOrigin //跨域
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    @ApiOperation(value = "根据课程id查询章节，小节信息")
    @GetMapping("getChapterVideoById/{courseId}")
    public R getChapterVideoById(@PathVariable String courseId) {
        List<ChapterVo> chapterList = new ArrayList<>();
        chapterList = eduChapterService.getChapterVideoById(courseId);
        return R.ok().data("chapterList", chapterList);
    }

    @ApiOperation(value = "添加章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter) {
        eduChapterService.save(eduChapter); //mp中自带保存方法
        return R.ok();
    }

    @ApiOperation(value = "根据id删除章节")
    @DeleteMapping("deleteChapterById/{id}")
    public R deleteChapterById(@PathVariable String id) {
        eduChapterService.removeById(id); //根据id删除
        return R.ok();
    }

    @ApiOperation(value = "根据id查询章节")
    @GetMapping("getChapterById/{id}")
    public R getChapterById(@PathVariable String id){
        EduChapter eduChapter = eduChapterService.getById(id);
        return R.ok().data("eduChapter", eduChapter);
    }

    @ApiOperation(value = "修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter) {
        eduChapterService.updateById(eduChapter);
        return R.ok();
    }

}

