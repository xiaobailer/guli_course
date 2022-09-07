package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-26
 */
@Api(description = "课时管理")
@RestController
//@CrossOrigin // 跨域问题
@RequestMapping("/eduservice/edu-video")
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;
            
    @ApiOperation(value = "添加小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "根据id删除小节")
    @DeleteMapping("deleteVideoById/{id}")
    //TODO 删除小节的时候，需要同时删除阿里云里面的视频
    public R deleteVideoById(@PathVariable String id) {
        eduVideoService.removeById(id); //根据id删除
        return R.ok();
    }

    @ApiOperation(value = "根据id查询小节,回显信息用")
    @GetMapping("getVideoById/{id}")
    public R getVideoById(@PathVariable String id){
        EduVideo eduVideo= eduVideoService.getById(id);
        return R.ok().data("eduVideo", eduVideo);
    }

    @ApiOperation(value = "修改小节")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }
}

