package com.blog.controller;

import com.blog.common.Result;
import com.blog.entity.User;
import com.blog.service.UserService;
import com.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return Result.error(401, "请先登录");
        }
        String actualToken = token.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(actualToken);
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    @PutMapping("/info")
    public Result<User> updateUserInfo(@RequestHeader(value = "Authorization", required = false) String token,
                                        @RequestBody User user) {
        if (token == null || token.isEmpty()) {
            return Result.error(401, "请先登录");
        }
        String actualToken = token.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(actualToken);
        user.setId(userId);
        User updatedUser = userService.updateUserInfo(user);
        return Result.success("更新成功", updatedUser);
    }
}
