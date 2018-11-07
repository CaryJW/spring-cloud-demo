# 基于哪个镜像
FROM java:8

# 将本地文件夹挂载到当前容器
VOLUME /tmp

ADD ./target/config-server-0.0.1-SNAPSHOT.jar config-server.jar

## 开放端口
#EXPOSE 7010

# 配置容器启动后执行的命令
ENTRYPOINT ["java","-jar","/config-server.jar"]