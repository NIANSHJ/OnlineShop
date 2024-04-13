package com.example.service.impl;

import com.example.service.CloudFileService;
import com.example.utils.QiNiuUtil;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class CloudFileServiceImpl implements CloudFileService {

    @Autowired
    private QiNiuUtil qiNiuUtil;

    @Value("${qiniu.directory}")
    private String directory;

    //判断文件合法性服务
    @Override
    public boolean check(MultipartFile file) {
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<>());
        Metadata metadata = new Metadata();
        metadata.add(TikaCoreProperties.RESOURCE_NAME_KEY, file.getName());
        try (InputStream stream = file.getInputStream()) {
            parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
            Pattern pattern = Pattern.compile("image/.*");
            Matcher matcher = pattern.matcher(metadata.get(HttpHeaders.CONTENT_TYPE));
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //文件上传服务
    @Override
    public String upload(MultipartFile file, String fileName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileSuffix = null;
        if (originalFilename != null) fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        fileName = directory + fileName + fileSuffix;
        return qiNiuUtil.upload(file.getBytes(), fileName);
    }

    //文件删除服务
    @Override
    public void delete(String fileName) throws IOException {
        fileName = directory + fileName.substring(fileName.lastIndexOf("/") + 1);
        qiNiuUtil.delete(fileName);
    }
}

