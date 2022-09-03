package com.atguigu.videoservice.controller;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.atguigu.baserservice.handler.GuliException;
import com.atguigu.commonutils.R;
import com.atguigu.videoservice.utils.AliyunVodSDKUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Api(description="视频管理")
@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VideoAdminController {

    @ApiOperation(value = "上传视频")
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file){
        try {
            AliyunVodSDKUtils.initVodClient("LTAI5tEeM6EFMnRZnigakmp9", "Mh8HwPhuoEoLFP8WBFtLPKHkLT3wAs");
            InputStream inputStream = file.getInputStream();
            System.out.println("inputStream = " + inputStream);
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0,originalFilename.lastIndexOf("."));
            UploadStreamRequest request = new UploadStreamRequest(
                    "LTAI5tEeM6EFMnRZnigakmp9",
                    "Mh8HwPhuoEoLFP8WBFtLPKHkLT3wAs",
                    title,
                    originalFilename,
                    inputStream
            );
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            String videoId = response.getVideoId();
            return R.ok().data("videoId",videoId);

        } catch (IOException | ClientException e) {
            e.printStackTrace();
            throw  new GuliException(20001,"上传视频失败");
        }
    }

    @ApiOperation(value = "删除视频")
    @DeleteMapping("delVideo/{videoId}")
    public R delVideo(@PathVariable("videoId") String videoId) {
        try {
            //初始化客户端对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient("LTAI5tEeM6EFMnRZnigakmp9", "Mh8HwPhuoEoLFP8WBFtLPKHkLT3wAs");
            //创建请求对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //创建响应对象
          //  DeleteVideoResponse response = new DeleteVideoResponse();
            //向请求设置参数
            request.setVideoIds(videoId);
            //调用客户端对象方法发送请求，拿到响应
            client.getAcsResponse(request);
            return R.ok();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败");
        }
    }
}
