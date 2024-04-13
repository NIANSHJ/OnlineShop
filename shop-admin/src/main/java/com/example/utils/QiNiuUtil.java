package com.example.utils;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Data
@ConfigurationProperties(prefix = "qiniu")
public class QiNiuUtil {

    private String accessKey;

    private String secretKey;

    private String bucketName;

    private String path;

    //上传文件
    public String upload(byte[] data, String fileName) throws IOException {
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(bucketName, fileName);
        Configuration configuration = new Configuration(Region.huanan());
        UploadManager uploadManager = new UploadManager(configuration);
        uploadManager.put(data, fileName, token);
        return path + fileName;
    }

    //删除文件
    public void delete(String fileName) throws IOException {
        Auth auth = Auth.create(accessKey, secretKey);
        Configuration configuration = new Configuration(Region.huanan());
        BucketManager bucketManager = new BucketManager(auth, configuration);
        bucketManager.delete(bucketName, fileName);
    }
}
