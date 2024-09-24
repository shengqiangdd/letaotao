package com.gxcy.letaotao.web.app.dto;

import com.gxcy.letaotao.common.enums.LTImagesType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传
 */
@Data
@NoArgsConstructor
public class LTImagesDTO {
    @NotNull(message = "文件不能为空")
    private MultipartFile file;
    private String module;
    private LTImagesType type;
    private Integer relatedId;
}
