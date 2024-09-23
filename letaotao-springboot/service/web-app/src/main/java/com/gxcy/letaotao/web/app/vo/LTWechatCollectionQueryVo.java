package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.enums.LTCollectionTargetType;
import lombok.Data;

@Data
public class LTWechatCollectionQueryVo {
    private String label;
    private Long userId;
    private LTCollectionTargetType targetType;
}
