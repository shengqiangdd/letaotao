package com.gxcy.letaotao.web.admin.dto;

import com.gxcy.letaotao.common.enums.LTImagesType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传
 */
@Data
public class LTImagesDTO {
    @NotNull(message = "文件不能为空")
    private MultipartFile file;
    private String module;
    private LTImagesType type;
    private Integer relatedId;
}
