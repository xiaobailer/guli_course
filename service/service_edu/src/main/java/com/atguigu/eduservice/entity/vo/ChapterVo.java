package com.atguigu.eduservice.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//创建vo类是为了跟前端数据对应
@Data
public class ChapterVo {

    private String id;

    private String title;

    private List<VideoVo> children = new ArrayList<>();
}
