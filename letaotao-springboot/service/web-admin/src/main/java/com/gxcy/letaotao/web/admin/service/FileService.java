package com.gxcy.letaotao.web.admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    /**
     * 文件上传
     *
     * @param file   文件上传对象
     * @param module 文件夹名称
     * @return
     */
    String upload(MultipartFile file, String module);

    /**
     * 删除文件
     *
     * @param url
     */
    void delete(String url);

    /**
     * 文件批量上传
     *
     * @param files  文件数组上传对象
     * @param module 文件夹名称
     * @return
     */
    List<String> multiUpload(MultipartFile[] files, String module);


}
