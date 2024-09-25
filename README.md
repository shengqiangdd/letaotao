## 乐淘淘

### 技术栈：

后端：Springboot + SpringSecurity + Mybatis-Plus + Redis + JWT + MySQL + Knife4j + WebSocket + AliyunOSS

前端：Vue + vue-element-admin + 微信小程序 + T-design

### 环境：

Maven3.9.8  JDK17  MySQL8  node18+ 



#### 功能介绍：

##### 后台管理系统：

系统管理：用户管理、角色管理、菜单管理。

乐淘淘管理：商品管理、帖子管理、用户管理。

##### 微信小程序：

闲置物品发布与管理、商品搜索与浏览、社区互动、交流下单。



#### 后端项目结构：

```
├─common
│  └─src
│      └─main
│          ├─java
│          │  └─com
│          │      └─gxcy
│          │          └─letaotao
│          │              └─common
│          │                  ├─config
│          │                  │  ├─oss
│          │                  │  └─redis
│          │                  ├─entity
│          │                  ├─enums
│          │                  ├─exception
│          │                  └─utils
└─service
    ├─web-admin
    │  └─src
    │      ├─main
    │      │  ├─java
    │      │  │  └─com
    │      │  │      └─gxcy
    │      │  │          └─letaotao
    │      │  │              └─web
    │      │  │                  └─admin
    │      │  │                      ├─config
    │      │  │                      │  ├─filter
    │      │  │                      │  ├─listener
    │      │  │                      │  ├─security
    │      │  │                      │  │  ├─handler
    │      │  │                      │  │  └─service
    │      │  │                      │  └─validator
    │      │  │                      ├─controller
    │      │  │                      ├─dto
    │      │  │                      ├─entity
    │      │  │                      ├─exception
    │      │  │                      ├─mapper
    │      │  │                      ├─service
    │      │  │                      │  └─impl
    │      │  │                      └─vo
    │      │  └─resources
    │      │      └─mapper
    └─web-app
        └─src
            ├─main
            │  ├─java
            │  │  └─com
            │  │      └─gxcy
            │  │          └─letaotao
            │  │              └─web
            │  │                  └─app
            │  │                      ├─config
            │  │                      │  ├─handler
            │  │                      │  ├─interceptor
            │  │                      │  └─validator
            │  │                      ├─controller
            │  │                      ├─dto
            │  │                      ├─entity
            │  │                      ├─mapper
            │  │                      ├─service
            │  │                      │  └─impl
            │  │                      ├─utils
            │  │                      └─vo
            │  └─resources
            │      └─mapper


```

