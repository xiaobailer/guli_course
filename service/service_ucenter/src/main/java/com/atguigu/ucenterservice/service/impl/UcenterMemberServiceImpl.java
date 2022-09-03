package com.atguigu.ucenterservice.service.impl;

import com.alibaba.nacos.common.util.Md5Utils;
import com.atguigu.baserservice.handler.GuliException;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.ucenterservice.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-02
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //用户注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取数据，验空。需要做一系列校验
        String mobile = registerVo.getMobile();
        String code = registerVo.getCode();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        //查询数据库中，验证手机号是否重复  验证数据是否为空，若有一个数据为空，都不能向下执行
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "注册信息缺失");
        }
        //验证手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count>0) {
            throw new GuliException(20001,"手机号重复");
        }
        //验证短信验证码 注意，在字符串比较的时候，属性.equals(另一个属性) 的时候，前面的那个属性不能为空
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)) {
            throw new GuliException(20001, "验证码不正确");
        }
        //明文密码传入数据库中，需要用到密文 使用MD5加密,这种加密不可逆
        String md5Password = MD5.encrypt(password);
        //补充信息插入数据库
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setNickname(nickname);
        ucenterMember.setPassword(md5Password);
        ucenterMember.setAvatar("https://guli-file22822.oss-cn-shenzhen.aliyuncs.com/6601.png"); //默认头像
        ucenterMember.setIsDisabled(false); //设置用户是否禁用
        baseMapper.insert(ucenterMember);
    }

    //用户登录
    @Override
    public String login(LoginVo loginVo) {
        //1.通过vo获取数据验空
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //2.根据手机号获取用户信息 为空，跳出
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "手机或密码有误");
        }
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        //根据手机获取用户信息
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if (ucenterMember == null) {
            throw new GuliException(20001, "手机或密码有误");
        }
        //3.密码加密后验证密码
        String encrypt = MD5.encrypt(password);
        if (!encrypt.equals(ucenterMember.getPassword())) {
            throw new GuliException(20001,"手机或密码有误");
        }
        //4.生成token字符串，返回 调用JwtUtils
        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
        return jwtToken;
    }
}
