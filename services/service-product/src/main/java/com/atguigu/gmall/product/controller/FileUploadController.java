package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.controller
 * @ClassName : FileUploadController.java
 * @createTime : 2022/11/3 15:08
 * @Description :文件上传
 */
@RestController
@RequestMapping("/admin/product")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file") MultipartFile file) throws Exception {
        //获取原始文件名：samsun.png
        String originalFilename = file.getOriginalFilename();
        //获取上传文件的大小
        long size = file.getSize();
        //TODO 保存到Minio，并返回文件的访问路径
        //获取文件的字节流
        String url = fileUploadService.uploadToMinio(file);

        return Result.ok(url);

    }

}
