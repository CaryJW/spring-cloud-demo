# 基于哪个镜像
FROM java:8

# 将本地文件夹挂载到当前容器
VOLUME /tmp

ADD ./target/zuul-server-0.0.1-SNAPSHOT.jar zuul-server.jar

# 配置容器启动后执行的命令
ENTRYPOINT ["java","-jar","/zuul-server.jar"]