package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.baserservice.handler.GuliException;

import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelObject;
import com.atguigu.eduservice.entity.vo.OneSubjectVo;
import com.atguigu.eduservice.entity.vo.TwoSubjectVo;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-23
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void addSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, ExcelObject.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001, "导入课程失败");
        }
    }
    @Override
    public List<OneSubjectVo> getAllSubject() {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        //查询一级目录
        wrapper.eq("parent_id", "0");
        List<EduSubject> oneEdusubject = baseMapper.selectList(wrapper);
        //查询二级目录
        QueryWrapper<EduSubject> wrapper1 = new QueryWrapper<>();
        wrapper1.ne("parent_id", "0");// 查询子级目录
        List<EduSubject> twoEdusubject = baseMapper.selectList(wrapper1);

        //封装一级目录
        List<OneSubjectVo> allEduSubject = new ArrayList<>();
        for (int i = 0; i < oneEdusubject.size(); i++) {
            //取出每个一级目录
            EduSubject oneSubject = oneEdusubject.get(i);
            OneSubjectVo oneSubjectVo = new OneSubjectVo();
            //EduSubject 转换成 oneSubject
            BeanUtils.copyProperties(oneSubject, oneSubjectVo);
            allEduSubject.add(oneSubjectVo);

            List<TwoSubjectVo> twoSubjectVos  = new ArrayList<>();
            for (int j = 0; j < twoEdusubject.size(); j++) {
                EduSubject twoSubject = twoEdusubject.get(j);
                if (twoSubject.getParentId().equals(oneSubjectVo.getId())) {
                    TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
                    BeanUtils.copyProperties(twoSubject, twoSubjectVo);
                    twoSubjectVos.add(twoSubjectVo);
                }
            }
            oneSubjectVo.setChildren(twoSubjectVos);
        }
        //封装二级目录
        
        return allEduSubject;
    }
}
