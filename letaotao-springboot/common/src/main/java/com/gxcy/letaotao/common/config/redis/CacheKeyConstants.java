package com.gxcy.letaotao.common.config.redis;

public final class CacheKeyConstants {

    // 防止外部实例化
    private CacheKeyConstants() {
        throw new AssertionError("Cannot instantiate constant utility class");
    }

    // 用户相关的缓存键
    public static final String ADMIN_USER = "admin_users";

    public static final String APP_USER = "wx_users";

    // 商品相关的缓存键
    public static final String PRODUCT = "products";

    // 图片相关的缓存键
    public static final String IMAGES = "images";

    // 帖子相关的缓存键
    public static final String POST = "posts";

    // 商品分类相关的缓存键
    public static final String CATEGORY = "categories";

    // 商品评价相关的缓存键
    public static final String EVALUATE = "evaluates";

    // 留言/评论相关的缓存键
    public static final String COMMENT = "comments";

    // 消息相关的缓存键
    public static final String MESSAGE = "messages";

    // 订单相关的缓存键
    public static final String ORDER = "orders";

    // 地址相关的缓存键
    public static final String ADDRESS = "addresses";

    // 聊天相关的缓存键
    public static final String CHAT_RELATION = "chat_relations";

    // 点赞相关的缓存键
    public static final String LIKE = "likes";

    // 收藏相关的缓存键
    public static final String COLLECTION = "collections";

    // 权限相关的缓存键
    public static final String PERMISSION = "permissions";

    // 角色相关的缓存键
    public static final String ROLE = "roles";

    // 字典相关的缓存键
    public static final String DICTIONARY = "dictionaries";

    public static final String ADMIN_TOKEN_PREFIX = "admin_token_";
    public static final String APP_TOKEN_PREFIX = "app_token_";

}