## 乐淘淘

### 技术栈：

后端：Springboot + SpringSecurity + Mybatis-Plus + Redis + JWT + MySQL + Knife4j + WebSocket + AliyunOSS

前端：Vue + vue-element-admin + 微信小程序 + T-design

### 环境：

Maven3.9.8  JDK17  MySQL8  node18+ 

使用Spring Boot快速搭建项目环境，使用nacos作为配置中心，使用Spring Validation进行数据校验，确保输入数据的正确性和完整性；编写特定的响应结果类Result，用于统一返回格式；实现全局异常处理器，统一处理系统中可能出现的各种异常；采用RESTful风格的接口设计。
使用vue-element-admin提供一套完整的管理界面，便于管理员操作和管理平台，提高开发效率；使用Spring Security实现用户登录认证和基于角色的访问控制（RBAC），确保系统的安全性，并使用BCryptPasswordEncoder对用户密码进行加密存储。
使用JWT生成登录后的token，并将其存储在Redis中，通过自定义拦截器验证每次请求的JWT token，确保用户认证状态的有效性。
使用MyBatis Plus简化数据库操作，提供基本的CRUD功能，提高开发效率，提高数据查询的灵活性。
使用阿里云OSS存储和管理用户头像、商品图片和帖子图片，确保数据的安全性和高效存取。
使用MySQL存储用户信息、商品信息等核心数据，确保数据的完整性和一致性。
使用Redis缓存商品信息等数据，提高系统的响应速度。
使用WebSocket实现实时通讯功能，支持用户即时聊天。
使用Knife4j生成详细的接口文档，方便前后端联调和后续维护，并提供直观的API文档展示。文档地址：ip地址:9999/doc.html
使用微信小程序官方API进行前端开发，使用T-design快速构建用户界面。

#### 主要功能：

##### 后台管理系统：

系统管理：用户管理、角色管理、菜单管理。

乐淘淘管理：商品管理、帖子管理、用户管理、数据统计。

##### 微信小程序：

闲置物品发布与管理、商品搜索与浏览、社区互动、交流下单。

