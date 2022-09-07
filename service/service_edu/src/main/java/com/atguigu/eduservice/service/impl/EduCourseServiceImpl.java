package com.atguigu.eduservice.service.impl;

import com.atguigu.baserservice.handler.GuliException;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-24
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //数据库中的一对一案例
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    //课程描述主键策略需要要手动配置，课程描述的主键，就是course的主键

    //添加课程信息
    @Override
    public String addCourseInfo(CourseInfoForm courseInfoForm) {
        //1.添加课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert == 0) {
            throw new GuliException(20001, "创建课程失败");
        }
        //2.获取课程id
        String courseId = eduCourse.getId();
        //3.添加课程描述
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseId);
        eduCourseDescription.setDescription(courseInfoForm.getDescription());
        //mbp自动实现插入
        eduCourseDescriptionService.save(eduCourseDescription);
        return courseId;
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {
        //根据id查询课程信息
        EduCourse eduCourse = baseMapper.selectById(id);
        //封装课程信息
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(eduCourse, courseInfoForm);
        //根据id查询课程描述信息
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(id);
        //封装课程描述信息
        courseInfoForm.setDescription(eduCourseDescription.getDescription());
        return courseInfoForm;
    }

    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm) {
        //获取网页回传的vo对象，封装成EduCourse
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm, eduCourse);
        //更新课程数据
        int update = baseMapper.updateById(eduCourse);
        //判断是否成功
        if (update == 0) {
            throw new GuliException(20001, "修改课程失败");
        }
        //更新课程描述
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoForm.getId());
        eduCourseDescription.setDescription(courseInfoForm.getDescription());
        eduCourseDescriptionService.updateById(eduCourseDescription);
    }

    @Override
    public CoursePublishVo getCursePublicById(String id) {
        CoursePublishVo coursePublishVo = baseMapper.getCursePublicById(id);
        return coursePublishVo;
    }


    //前台分页查询课程信息
    @Override
    public Map<String, Object> getCourseApiPageVo(Page<EduCourse> pageParam, CourseQueryVo courseQueryVo) {
        //1.取出查询信息
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String subjectId = courseQueryVo.getSubjectId();
        String buyCountSort = courseQueryVo.getBuyCountSort();
        String gmtCreateSort = courseQueryVo.getGmtCreateSort();
        String priceSort = courseQueryVo.getPriceSort();
        //2.判断信息是否为空
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(buyCountSort)) {
            queryWrapper.orderByDesc("buy_count", buyCountSort);
        }
        if (!StringUtils.isEmpty(gmtCreateSort)) {
            //倒排序
            queryWrapper.orderByDesc("gmt_create", gmtCreateSort);
        }
        if (!StringUtils.isEmpty(priceSort)) {
            queryWrapper.orderByDesc("price", priceSort);
        }

        //3.分页查询
        baseMapper.selectPage(pageParam, queryWrapper);
        //4.封装数据
        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;

    }

    @Override
    public CourseWebVo getCourseVo(String courseId) {
        CourseWebVo courseWebVo = baseMapper.getCourseVo(courseId);
        return courseWebVo;
    }
}
