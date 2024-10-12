# 指定基础镜像
FROM ringcentral/jdk:17

# 设置环境变量PARAMS，包含了应用程序启动时所需的参数
ENV PARAMS="--server.port=8080 --spring.profiles.active=prod --spring.cloud.nacos.config.server-addr=ltt-nacos.ltt:8848 --spring.cloud.nacos.config.namespace=prod --spring.cloud.nacos.config.file-extension=properties"

# 设置时区为上海时区
# 这一步确保了容器内的日期和时间与预期一致
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo 'Asia/Shanghai' > /etc/timezone

# 复制构建好的jar包到容器内
COPY target/*.jar /app.jar

# 暴露应用程序监听的端口
EXPOSE 8080

# 定义容器启动后执行的命令
# 使用/bin/sh -c 来允许更多的灵活性，如传递参数等
ENTRYPOINT ["/bin/sh", "-c", "java -jar /app.jar ${PARAMS}"]