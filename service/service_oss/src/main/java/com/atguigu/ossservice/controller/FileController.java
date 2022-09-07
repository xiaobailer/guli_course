package com.atguigu.ossservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.ossservice.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(description = "文件管理")
@RequestMapping("/ossservice/fileoss")
//@CrossOrigin //跨域操作
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "上传文件")
    @PostMapping("uploadFile")
    public R uploadFile(MultipartFile file) {
        //1.获取文件

        //2.调用接口上传文件
        System.out.println(file);
        String url = fileService.uploadFileOss(file);

        //3.获取上传后的链接，然后存储到数据库
        return R.ok().data("url",url);
    }
}
