package com.example.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudFileService {
    //判断文件合法性服务
    boolean check(MultipartFile file);

    //文件上传服务
    String upload(MultipartFile file, String time) throws IOException;

    //文件删除服务
    void delete(String fileName) throws IOException;
}
