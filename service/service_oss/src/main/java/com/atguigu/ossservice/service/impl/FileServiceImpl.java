package com.atguigu.ossservice.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.atguigu.baserservice.handler.GuliException;
import com.atguigu.ossservice.service.FileService;
import com.atguigu.ossservice.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    //上传文件 参考阿里云
    @Override
    public String uploadFileOss(MultipartFile multipartFile) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        // String endpoint = "oss-cn-shenzhen.aliyuncs.com";  配置到配置信息中，封装成utils再进行调用
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
       // String accessKeyId = "LTAI5tEeM6EFMnRZnigakmp9";
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
     //   String accessKeySecret = "Mh8HwPhuoEoLFP8WBFtLPKHkLT3wAs";
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "guli-file22822";
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        String fileName = multipartFile.getOriginalFilename();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);


        try {
            //上传文件流
            InputStream inputStream = multipartFile.getInputStream();
            //优化文件名,文件名不重复
            fileName =  UUID.randomUUID().toString() + fileName;

            //优化文件存储路径(2022/08/22/uuid+filename)
            String dateTime = new DateTime().toString("yyyy/MM/dd");
            fileName = dateTime + "/" + fileName;
            ossClient.putObject(bucketName, fileName, inputStream);
            //关闭流
            ossClient.shutdown();
            //https://guli-file201021.oss-cn-beijing.aliyuncs.com/01.jpg  返回的url
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001, "上传失败");
        }

        }
    }

