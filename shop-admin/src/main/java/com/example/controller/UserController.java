package com.example.controller;

import com.example.aop.Logging;
import com.example.pojo.Result;
import com.example.pojo.User;
import com.example.pojo.ValidUser;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    //用户登录
    @PostMapping("/login")
    @Logging(module = "用户", operation = "登录")
    public Result<?> login(@RequestBody ValidUser validUser) {
        String username = validUser.getUsername();
        String password = validUser.getPassword();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.
                authenticate(usernamePasswordAuthenticationToken);
        User loginUser = (User) authentication.getPrincipal();
        String token = userService.login(loginUser);
        validUser.setUsername(loginUser.getUsername());
        return Result.success(token, validUser);
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo() {
        String username = userService.getUserName();
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin:center')")
    @Logging(module = "管理员", operation = "更新信息")
    public Result<?> update(@RequestBody @Validated User user) {
        userService.updateInfo(user);
        return Result.success();
    }

    @PatchMapping("/password")
    @PreAuthorize("hasAuthority('admin:center')")
    @Logging(module = "管理员", operation = "更新密码")
    public Result<?> updatePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        String rePassword = params.get("rePassword");
        if (StringUtils.hasLength(oldPassword) && StringUtils.hasLength(newPassword)
                && StringUtils.hasLength(rePassword) && rePassword.equals(newPassword)) {
            String username = userService.getUserName();
            User loginUser = userService.findByUserName(username);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(oldPassword, loginUser.getPassword())) {
                userService.updatePassword(username, newPassword);
                return Result.success();
            } else return Result.error("原密码填写不正确");
        } else return Result.error("请检查输入");
    }

    @PostMapping("/logOut")
    @Logging(module = "用户", operation = "退出登录")
    public Result<?> logOut() {
        return Result.success();
    }

}
