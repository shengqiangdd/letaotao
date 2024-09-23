package com.gxcy.letaotao.web.admin.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.gxcy.letaotao.common.config.oss.OssProperties;
import com.gxcy.letaotao.web.admin.service.FileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl implements FileService {

    @Resource
    private OssProperties ossProperties;

    /**
     * 文件上传
     *
     * @param file   文件上传对象
     * @param module 文件夹名称
     * @return
     */
    @Override
    public String upload(MultipartFile file, String module) {
        // 获取地域节点
        String endPoint = ossProperties.getEndPoint();
        // 获取AccessKeyId
        String keyId = ossProperties.getKeyId();
        // 获取AccessKeySecret
        String keySecret = ossProperties.getKeySecret();
        // 获取BucketName
        String bucketName = ossProperties.getBucketName();
        try {
            // 创建OSSClient实例
            OSS ossClient = new OSSClientBuilder().build(endPoint, keyId, keySecret);
            // 上传文件流
            InputStream inputStream = file.getInputStream();
            // 获取旧名称
            String originalFilename = file.getOriginalFilename();
            // 获取文件后缀名
            String extension = FilenameUtils.getExtension(originalFilename);
            // 将文件名重命名
            String newFileName = UUID.randomUUID().toString()
                    .replace("-", "") + "." + extension;
            // 使用当前日期进行分类管理
            String datePath = new DateTime().toString("yyyy/MM/dd");
            // 构建文件名
            newFileName = module + "/" + datePath + "/" + newFileName;
            // 调用OSS文件上传的方法
            ossClient.putObject(bucketName, newFileName, inputStream);
            // 关闭OSSClient
            ossClient.shutdown();
            String fileName = "https://" + bucketName + "." + endPoint + "/" + newFileName;
            log.info("文件上传成功：{}", fileName);
            // 返回文件地址
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param url
     */
    @Override
    public void delete(String url) {
        // 获取地域节点
        String endPoint = ossProperties.getEndPoint();
        // 获取AccessKeyId
        String keyId = ossProperties.getKeyId();
        // 获取AccessKeySecret
        String keySecret = ossProperties.getKeySecret();
        // 获取BucketName
        String bucketName = ossProperties.getBucketName();
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endPoint, keyId, keySecret);
        // 组装文件地址
        String host = "https://" + bucketName + "." + endPoint + "/";
        // 获取文件名
        String objectName = url.substring(host.length());
        try {
            // 删除文件
            ossClient.deleteObject(bucketName, objectName);
            log.info("文件删除成功：{}", host + objectName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 文件批量上传
     *
     * @param files  文件数组上传对象
     * @param module 文件夹名称
     * @return
     */
    @Override
    public List<String> multiUpload(MultipartFile[] files, String module) {
        if (files != null && files.length > 0) {
            String endPoint = ossProperties.getEndPoint();
            String keyId = ossProperties.getKeyId();
            String keySecret = ossProperties.getKeySecret();
            String bucketName = ossProperties.getBucketName();
            // 创建OSSClient实例
            OSS ossClient = new OSSClientBuilder().build(endPoint, keyId, keySecret);
            List<String> urls = new ArrayList<>();
            try {
                for (MultipartFile file : files) {
                    // 上传文件流
                    InputStream inputStream = file.getInputStream();
                    // 获取文件原名
                    String originalFilename = file.getOriginalFilename();
                    // 获取文件后缀名
                    String extension = FilenameUtils.getExtension(originalFilename);
                    // 将文件名重命名
                    String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
                    // 使用当前日期进行分类管理
                    String datePath = new DateTime().toString("yyyy/MM/dd");
                    // 构建文件名
                    newFileName = module + "/" + datePath + "/" + newFileName;
                    // 调用OSS文件上传的方法
                    ossClient.putObject(bucketName, newFileName, inputStream);
                    // 关闭OSSClient
                    ossClient.shutdown();
                    // 返回文件地址
                    String url = "https://" + bucketName + "." + endPoint + "/" + newFileName;
                    urls.add(url);
                }
                log.info("文件上传成功：{}", urls);
                return urls;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

}
