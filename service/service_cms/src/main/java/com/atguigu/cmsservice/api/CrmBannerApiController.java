package com.atguigu.cmsservice.api;

import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "前台banner展示")
@RestController
@RequestMapping("/cmsservice/banner")
@CrossOrigin
public class CrmBannerApiController {
    @Autowired
    private CrmBannerService crmBannerService;

    @ApiOperation(value = "查询所有banner信息")
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> banners = crmBannerService.getAllBanner();
        return R.ok().data("banners", banners);
    }
}
