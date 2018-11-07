# 基于哪个镜像
FROM java:8

# 将本地文件夹挂载到当前容器
VOLUME /tmp

# 拷贝文件到容器，也可以直接写成ADD eureka-server-0.0.1-SNAPSHOT.jar /eureka-server.jar
ADD eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar
#RUN bash -c 'touch /eureka-server.jar'

# 开放端口
EXPOSE 7000
EXPOSE 7001
EXPOSE 7002

# 配置容器启动后执行的命令
ENTRYPOINT ["java","-jar","/eureka-server.jar"]