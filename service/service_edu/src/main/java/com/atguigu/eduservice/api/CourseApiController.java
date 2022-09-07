package com.atguigu.eduservice.api;

import com.alibaba.excel.event.Order;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.vo.CourseWebVoForOrder;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description="前台课程展示")
@RestController
@RequestMapping("/eduservice/courseapi")
//@CrossOrigin
public class CourseApiController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    @ApiOperation(value = "带条件分页查询课程列表")
    @PostMapping("getCourseApiPageVo/{current}/{limit}")
    public R getCourseApiPageVo(@PathVariable Long current,
                                @PathVariable Long limit,
                                @RequestBody CourseQueryVo courseQueryVo){
        Page<EduCourse> page = new Page<>(current,limit);
        Map<String,Object> map = courseService.getCourseApiPageVo(page,courseQueryVo);
        return R.ok().data(map);
    }


/*
    @ApiOperation(value = "根据课程id查询课程相关信息")
    @GetMapping("getCourseWebInfo/{courseId}")
    public R getCourseWebInfo(@PathVariable String courseId) {
        //1.查询课程相关信息存入CourseWebVo
        CourseWebVo courseWebVo = courseService.getCourseVo(courseId);
        //2.查询课程大纲信息
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoById(courseId);
        return R.ok().data("courseWebVo", courseWebVo).data("chapterVideoList", chapterVideoList);
    }
*/

    @ApiOperation(value = "【跨模块调用根据课程id课程查询相关信息】")
    @GetMapping("getCourseInfoForOrder/{courseId}") //注意【远程调用】的时候传入的参数记得要加@PathiVariable注解
    public CourseWebVoForOrder getCourseInfoForOrder(@PathVariable("courseId") String courseId){
        //1.查询课程相关信息存入CourseWebVo
        CourseWebVo courseWebVo = courseService.getCourseVo(courseId);
        CourseWebVoForOrder courseWebVoForOrder = new CourseWebVoForOrder();
        BeanUtils.copyProperties(courseWebVo, courseWebVoForOrder);
        return courseWebVoForOrder;
    }

    @ApiOperation(value = "根据课程id查询课程相关信息")
    @GetMapping("getCourseWebInfo/{courseId}")
    public R getCourseWebInfo(@PathVariable String courseId, HttpServletRequest request){
        //1 查询课程相关信息存入CourseWebVo
        CourseWebVo courseWebVo = courseService.getCourseVo(courseId);
        //2查询课程大纲信息
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoById(courseId);
        //3根据课程id、用户id查询是已购买,远程调用
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("memberId ="+memberId);
        boolean isBuyCourse = orderClient.isBuyCourse(courseId, memberId);

        return R.ok().data("courseWebVo",courseWebVo)
                .data("chapterVideoList",chapterVideoList)
                .data("isBuyCourse",isBuyCourse);

    }


}
