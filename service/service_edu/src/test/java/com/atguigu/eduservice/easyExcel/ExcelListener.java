package com.atguigu.eduservice.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
//创建读的监听器
public class ExcelListener extends AnalysisEventListener<Demodata> {
    //读取每行数据
    @Override
    public void invoke(Demodata demodata, AnalysisContext analysisContext) {
        System.out.println(demodata);
    }
    //数据读完的操作
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
