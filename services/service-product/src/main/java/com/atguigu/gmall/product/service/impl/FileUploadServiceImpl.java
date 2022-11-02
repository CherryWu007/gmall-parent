package com.atguigu.gmall.product.service.impl;


import com.atguigu.gmall.common.config.minio.properties.AppMinioProperties;

import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.service.impl
 * @ClassName : FileUploadServiceImpl.java
 * @createTime : 2022/11/3 15:16
 * @Description :
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {


    @Autowired
    private AppMinioProperties appMinioProperties;
    @Autowired
    private MinioClient minioClient;
    @Override
    public String uploadToMinio(MultipartFile file) throws
            IOException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            InvalidResponseException,
            XmlParserException,
            InternalException,
            RegionConflictException,
            InvalidBucketNameException,
            InsufficientDataException,
            ErrorResponseException {
        //1、创建出minioClient

        //2、判断桶是否存在

            if (!minioClient.bucketExists(appMinioProperties.getBucket())) {
                minioClient.makeBucket(appMinioProperties.getBucket());

            }
        //3、上传文件

        String date= DateUtil.formatDate(new Date());

        String ObjectName =date+"/"+UUID.randomUUID().toString().replace("-","")
                +"_"+
                file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        //参数

        PutObjectOptions options = new PutObjectOptions(inputStream.available(), -1L);
        options.setContentType(file.getContentType());
        minioClient.putObject(appMinioProperties.getBucket(),ObjectName,inputStream,options);

        //4、返回url的地址
        String url =appMinioProperties.getEndpoint()+"/"+appMinioProperties.getBucket()+"/"+ObjectName;
        return url;
    }
}
