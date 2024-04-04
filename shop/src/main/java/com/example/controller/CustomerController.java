package com.example.controller;

import com.example.aop.CLogging;
import com.example.pojo.Customer;
import com.example.pojo.Result;
import com.example.pojo.ValidUser;
import com.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/customer")
@Validated
public class CustomerController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerService customerService;

    //顾客注册
    @PostMapping("/register")
    @CLogging(module = "顾客", operation = "注册")
    public Result<?> register(@RequestBody @Validated ValidUser validUser) {
        String username = validUser.getUsername();
        String password = validUser.getPassword();
        if (customerService.verifyCode(username, validUser.getCode())) {
            customerService.register(username, password);
            return Result.success();
        } else return Result.error("验证码错误");
    }

    //向顾客邮箱发送验证码
    @PostMapping("/email")
    @CLogging(module = "顾客", operation = "邮箱验证码")
    public Result<?> sendEmail(@RequestBody Map<String, String> params) {
        String to = params.get("username");
        if (customerService.findByUserName(to) == null) {
            String code = customerService.code(to);
            customerService.sendEmail(to, code);
            return Result.success();
        } else return Result.error("此邮箱已经注册");
    }

    //顾客登录
    @PostMapping("/login")
    @CLogging(module = "顾客", operation = "登录")
    public Result<?> login(@RequestBody @Validated ValidUser validUser) {
        String username = validUser.getUsername();
        String password = validUser.getPassword();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.
                authenticate(usernamePasswordAuthenticationToken);
        Customer loginCustomer = (Customer) authentication.getPrincipal();
        String token = customerService.login(loginCustomer);
        validUser.setUsername(loginCustomer.getUsername());
        return Result.success(token, validUser);
    }

    //获取顾客信息
    @GetMapping("/customerInfo")
    public Result<Customer> customerInfo() {
        String username = customerService.getUserName();
        Customer customer = customerService.findByUserName(username);
        return Result.success(customer);
    }

    //更新顾客信息
    @PutMapping("/update")
    @CLogging(module = "顾客", operation = "更新信息")
    public Result<?> update(@RequestBody @Validated Customer customer) {
        customerService.updateInfo(customer);
        return Result.success();
    }

    //更新顾客密码
    @PatchMapping("/password")
    @CLogging(module = "顾客", operation = "更新密码")
    public Result<?> updatePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        String rePassword = params.get("rePassword");
        if (StringUtils.hasLength(oldPassword) && StringUtils.hasLength(newPassword)
                && StringUtils.hasLength(rePassword) && rePassword.equals(newPassword)) {
            String username = customerService.getUserName();
            Customer loginCustomer = customerService.findByUserName(username);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(oldPassword, loginCustomer.getPassword())) {
                customerService.updatePassword(username, newPassword);
                return Result.success();
            } else return Result.error("原密码填写不正确");
        } else return Result.error("请检查输入");
    }

    @PostMapping("/logOut")
    @CLogging(module = "顾客", operation = "退出登录")
    public Result<?> logOut() {
        return Result.success();
    }

}
