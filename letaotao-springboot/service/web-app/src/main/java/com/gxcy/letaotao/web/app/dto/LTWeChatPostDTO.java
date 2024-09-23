package com.gxcy.letaotao.web.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gxcy.letaotao.common.enums.LTPostStatus;
import com.gxcy.letaotao.web.app.vo.LTImagesVo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 微信小程序帖子传输对象
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LTWeChatPostDTO {
    private Integer id;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotEmpty(message = "帖子标题不能为空")
    private String title;

    @NotEmpty(message = "帖子内容不能为空")
    private String content;

    private String imageUrl;
    private LTPostStatus status;
    private List<LTImagesVo> newImages;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postTime;
}
