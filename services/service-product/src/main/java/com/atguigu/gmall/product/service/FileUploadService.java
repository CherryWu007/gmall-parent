package com.atguigu.gmall.product.service;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.service
 * @ClassName : FileUploadService.java
 * @createTime : 2022/11/3 15:15
 * @Description :
 */

public interface FileUploadService {
    /**
     * 将文件传输给Minio
     * @param file
     * @return
     */
    String uploadToMinio(MultipartFile file) throws InvalidPortException, InvalidEndpointException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, RegionConflictException;
}
