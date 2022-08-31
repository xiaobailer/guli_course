package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-26
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    EduVideoService eduVideoService;
    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {
        //根据courseId查询章节集合信息
        QueryWrapper<EduChapter> characterWrapper = new QueryWrapper<>();
        characterWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(characterWrapper);
        //根据courseId查询小节集合信息
        QueryWrapper<EduVideo> eduVideoWrapper = new QueryWrapper<>();
        eduVideoWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideos = eduVideoService.list(eduVideoWrapper);
        //遍历章节信息进行封装
        List<ChapterVo> eduChapterAll = new ArrayList<>();
        for (int i = 0; i < eduChapterList.size(); i++) {
            ChapterVo chapterVo = new ChapterVo();
            EduChapter eduChapter = eduChapterList.get(i);
            BeanUtils.copyProperties(eduChapter, chapterVo);
            eduChapterAll.add(chapterVo);
            //遍历和此章节的小节信息进行封装
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int j = 0; j < eduVideos.size(); j++) {
                EduVideo eduVideo = eduVideos.get(j);
                if (eduChapter.getId().equals(eduVideo.getChapterId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoVoList.add(videoVo);
                }
                chapterVo.setChildren(videoVoList);
            }
        }
        return eduChapterAll;
    }
}
