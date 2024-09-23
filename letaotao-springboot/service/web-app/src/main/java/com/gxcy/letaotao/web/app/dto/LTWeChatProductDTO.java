package com.gxcy.letaotao.web.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gxcy.letaotao.common.enums.LTProductStatus;
import com.gxcy.letaotao.web.app.vo.LTImagesVo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 微信商品传输对象
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LTWeChatProductDTO {
    private Integer id;

    @NotNull(message = "发布者ID不能为空")
    private Long publisherId;

    @NotEmpty(message = "商品名称不能为空")
    private String name;

    @NotEmpty(message = "商品描述不能为空")
    private String description;

    @NotNull(message = "商品分类ID不能为空")
    private Integer categoryId;

    @NotEmpty(message = "商品成色不能为空")
    private String condition;

    @NotNull(message = "商品价格不能为空")
    @Positive(message = "商品价格必须为正数")
    private BigDecimal price;

    private LTProductStatus status;
    private List<LTImagesVo> newImages;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
}
