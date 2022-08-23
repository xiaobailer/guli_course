package com.atguigu.eduservice.easyExcel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExcelTest {
    @Test //读操作，需要一个创建监听器
    public void readTesT() {
        String fileName = "C:\\Users\\42092\\Desktop\\easyExcelTest\\write.xlsx";
        EasyExcel.read(fileName, Demodata.class, new ExcelListener()).sheet().doRead();
    }


    @Test //写操作
    public void writeTest() {
        //写法1
        String fileName = "C:\\Users\\42092\\Desktop\\easyExcelTest\\write.xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName,Demodata.class).sheet("学生列表").doWrite(data());
    }

    private static List<Demodata> data() {
        List<Demodata> list = new ArrayList<Demodata>();
        for (int i = 0; i < 10; i++) {
            Demodata data = new Demodata();
            data.setSno(i);
            data.setSname("张三"+i);
            list.add(data);
        }
        return list;
    }

}



