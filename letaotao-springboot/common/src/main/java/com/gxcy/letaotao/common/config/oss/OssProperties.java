package com.gxcy.letaotao.common.config.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss.file")
public class OssProperties {
    private String endPoint;//地域节点
    private String keyId;//accessKeyId
    private String keySecret;//accessKeySecret
    private String bucketName;//bucketName
}
