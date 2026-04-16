package com.blog.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.common.BizException;
import com.blog.common.ResponseCode;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import com.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public User register(String username, String password, String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BizException(ResponseCode.PARAM_ERROR.getCode(), "用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(SecureUtil.md5(password));
        user.setEmail(email);
        user.setNickname(username);
        user.setStatus(1);
        user.setIsDeleted(0);

        userMapper.insert(user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new BizException(ResponseCode.UNAUTHORIZED.getCode(), "用户不存在");
        }

        if (!user.getPassword().equals(SecureUtil.md5(password))) {
            throw new BizException(ResponseCode.UNAUTHORIZED.getCode(), "密码错误");
        }

        if (user.getStatus() == 0) {
            throw new BizException(ResponseCode.FORBIDDEN.getCode(), "用户已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        redisTemplate.opsForValue().set("token:" + user.getId(), token, 24, TimeUnit.HOURS);
        return token;
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException(ResponseCode.NOT_FOUND.getCode(), "用户不存在");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public User updateUserInfo(User user) {
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            throw new BizException(ResponseCode.NOT_FOUND.getCode(), "用户不存在");
        }

        if (user.getNickname() != null) {
            existingUser.setNickname(user.getNickname());
        }
        if (user.getAvatar() != null) {
            existingUser.setAvatar(user.getAvatar());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getDescription() != null) {
            existingUser.setDescription(user.getDescription());
        }

        userMapper.updateById(existingUser);
        existingUser.setPassword(null);
        return existingUser;
    }
}
