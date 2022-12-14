package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.vo.UcenterMemberForOrder;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-09-02
 */
@Api(description = "前台用户会员管理")
@RestController
@RequestMapping("/ucenterservice/ucenter-member")
//@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;


    @ApiOperation(value = "用户注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo) {
        //调用service服务
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("login") //需要封装数据
    public R login(@RequestBody LoginVo loginVo) {
        //调用service服务
        String token = ucenterMemberService.login(loginVo);
        //返回token，便于其他服务调用，单点登录使用
        return R.ok().data("token",token);
    }

    //登录后，根据token获取用户信息
    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("getUcenterByToken") //需要整个请求，工具类中的方法 就是需要整个请求request
    public R getUcenterByToken(HttpServletRequest request) {
        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember ucenterMember = ucenterMemberService.getById(memberIdByJwtToken);
        return R.ok().data("ucenterMember", ucenterMember);
    }

    //跨模块调用查询用户信息
    @ApiOperation(value = "【跨模块调用根据memeberId获取用户信息】")
    @GetMapping("getUcenterForOrder/{memberId}") //注意【远程调用】的时候传入的参数记得要加@PathiVariable注解
    public UcenterMemberForOrder getUcenterForOrder(@PathVariable("memberId") String memberId) {
        UcenterMember ucenterMember = ucenterMemberService.getById(memberId);
        UcenterMemberForOrder ucenterMemberForOrder = new UcenterMemberForOrder();
        BeanUtils.copyProperties(ucenterMember, ucenterMemberForOrder);
        return ucenterMemberForOrder;
    }

    //统计注册人数【远程调用】
    @ApiOperation(value = "统计注册人数【远程调用】")
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable("day") String day) {
        Integer count = ucenterMemberService.countRegister(day);
        return R.ok().data("count", count);
    }

}

