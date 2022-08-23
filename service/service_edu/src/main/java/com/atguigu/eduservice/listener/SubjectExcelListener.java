package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.baserservice.handler.GuliException;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelObject;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListener extends AnalysisEventListener<ExcelObject> {

    //手动注入
    public EduSubjectService subjectService;

    public SubjectExcelListener() {}
    //创建有参数构造，传递subjectService用于操作数据库
    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }


    @Override
    public void invoke(ExcelObject excelObject, AnalysisContext analysisContext) {
        //1.数据是否为空
        if (excelObject == null) {
            throw new GuliException(20001, "导入课程失败");
        }

        EduSubject eduOneSubject = this.existOneSubject(subjectService, excelObject.getOneSubjectName());
        //2.判断一级分类的名称是否重复
        if (eduOneSubject == null) {
            eduOneSubject = new EduSubject();
            eduOneSubject.setTitle(excelObject.getOneSubjectName());
            eduOneSubject.setId("0");
            subjectService.save(eduOneSubject);
        }

        //3.二级不重复插入数据库
        EduSubject eduTwoSubject = this.existTwoSubject(subjectService, excelObject.getTwoSubjectName(), eduOneSubject.getId());
        //4.判断二级分类的名称是否重复
        if (eduTwoSubject == null) {
            eduTwoSubject = new EduSubject();
            eduTwoSubject.setTitle(excelObject.getTwoSubjectName());
            eduTwoSubject.setId("0");
            subjectService.save(eduTwoSubject);
        }
    }

    private EduSubject existOneSubject(EduSubjectService subjectService, String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", "name");
        wrapper.eq("parent_id", "0");
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }
    private EduSubject existTwoSubject(EduSubjectService subjectService, String name,String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", "name");
        wrapper.eq("parent_id", pid);
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
