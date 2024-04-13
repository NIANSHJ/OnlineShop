package com.example.controller;

import com.example.pojo.Result;
import com.example.service.CloudFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class CloudFileController {

    @Autowired
    private CloudFileService cloudFileService;

    //上传图片文件
    @PostMapping
    @PreAuthorize("hasAuthority('seller:goods:update')")
    public Result<?> upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("fileName") String fileName) throws IOException {
        if (file.isEmpty()) return Result.error("无法上传空文件");
        if (cloudFileService.check(file)) {
            String url = cloudFileService.upload(file, fileName);
            return Result.success(url);
        } else return Result.error("只支持图片格式的文件");
    }
}
