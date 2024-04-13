package com.example.controller;

import com.example.aop.Logging;
import com.example.pojo.Result;
import com.example.pojo.User;
import com.example.pojo.ValidUser;
import com.example.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping
    @PreAuthorize("hasAuthority('admin:seller')")
    @Logging(module = "人员", operation = "新增销售员")
    public Result<?> add(@RequestBody ValidUser validUser) {
        staffService.add(validUser);
        return Result.success();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin:seller')")
    @Logging(module = "人员", operation = "查询销售员")
    public Result<List<User>> list() {
        List<User> sellers = staffService.list();
        return Result.success(sellers);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:seller')")
    @Logging(module = "人员", operation = "删除销售员")
    public Result<?> delete(Integer id) {
        staffService.delete(id);
        return Result.success();
    }

}
