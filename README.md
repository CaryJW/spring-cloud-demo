## 总架构图
![img](doc/spring-cloud.jpg)

---

> 学习博客：http://www.mooooc.com/spring-cloud.html  
https://blog.csdn.net/forezp/article/details/70148833  
spring boot 版本2.0以上  
服务启动顺序，eureka-server 》config-server 》admin-server 》admin-client 。。。。

### 一、eureka 服务的注册与发现
#### ①依赖
```java
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

#### ②开启服务注册  
    通过 *@EnableEurekaServer* 注解启动一个服务注册中心提供给其他应用进行对话,  
    这个注解需要在springboot工程的启动application类上加
````java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}
````
#### ③单点配置
application.yml
````java
spring:
  application:
    name: eureka-server
server:
  port: 7000
eureka:
  client:
    register-with-eureka: false # 不向注册中心注册自己
    fetch-registry: false # 表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
      defaultZone: http://peer2:7004/eureka/ # 地址
````

#### ④集群配置(去除register-with-eureka: false,fetch-registry: false)
- application.yml
````java
spring:
  application:
    name: eureka-server
  profiles:
    active: peer1
info:
  version: @project.version@ # 显示版本信息
````
- application-peer1.yml
````java
server:
  port: 7000
spring:
  profiles: peer1
eureka:
  instance:
    hostname: peer1
  client:
#    register-with-eureka: false # 不向注册中心注册自己
#    fetch-registry: false # 表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
      defaultZone: http://peer2:7004/eureka/ # 地址

````
- application-peer2.yml
````java
server:
  port: 7004
spring:
  profiles: peer2
eureka:
  instance:
    hostname: peer2
  client:
#    register-with-eureka: false # 不向注册中心注册自己
#    fetch-registry: false # 表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
      defaultZone: http://peer1:7000/eureka/ # 地址
````
- host转换
在hosts文件中加入如下配置
````java
127.0.0.1 peer1
127.0.0.1 peer2
````

- 开启ip地址配置，通过ip地址注册则无需添加host
````java
eureka:
  instance:
    prefer-ip-address: true #是否显示IP地址
````

- 启动两个eureka-server
 
#### ⑤结果
- 访问 http://localhost:7000/
![img](doc/peer1.png)
- 访问 http://localhost:7004/
![img](doc/peer2.png)

### 二、服务提供者
#### ①依赖
````java
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
````
#### ②启动类
springboot工程的启动application类上加@EnableEurekaClient或者@EnableDiscoveryClient注解
````java
@SpringBootApplication
@EnableEurekaClient
@RestController
public class AdminClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminClientApplication.class, args);
    }
    
    @Value("${server.port}")
    private String port;

    @RequestMapping("/hello")
    public String like(@RequestParam(value = "name") String name) {
        return "Hello," + name + ".my port is:" + port;
    }
}

````
### ③配置
````java
server:
  port: 7010
spring:
  application:
    name: admin-client
eureka:
  client:
    healthcheck:
      enabled: true # 开启健康检查（依赖spring-boot-starter-actuator）
    service-url:
          defaultZone: http://localhost:7000/eureka/ # 注册到其中一个eureka-server即可
  instance:
    prefer-ip-address: true #是否显示IP地址
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    lease-expiration-duration-in-seconds: 30 # 发呆时间，即服务续约到期时间（缺省为90s）
    lease-renewal-interval-in-seconds: 10 # 心跳时间，即服务续约间隔时间（缺省为30s）
````

### 三、服务消费者
> rest+ribbon 
#### ①依赖
````java
<!--客户端的负载均衡依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>

<!--断路器-->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
````
#### ②启动类
````java
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix // 开启短路器
public class ServerConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerConsumerApplication.class, args);
	}

	@Bean
    @LoadBalanced // 开启负载均衡功能 轮询
    RestTemplate restTemplate(){
	    return new RestTemplate();
    }
}
````
#### ③server层
````java
@RestController
public class HelloController {
    @Autowired
    HelloService helloService;

    @RequestMapping("/hello")
    public String hi(@RequestParam(value = "name",defaultValue = "flyxiaozhu") String name){
        return helloService.hiService(name);
    }
}
````

#### ④controller层
````java
@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError") // 熔断器
    public String hiService(String name) {
        return restTemplate.getForObject("http://admin-client/hello?name=" + name, String.class);
    }

    public String hiError(String name) {
        return "Hi," + name + ".sorry!!!error...";
    }
}
````

> feign
#### ①依赖
````java
<!--feign 消费者依赖 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!--断路器-->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
````
#### ②启动类
````java
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ServerFeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerFeignApplication.class, args);
	}
}
````

#### ③server层
- Hiserver 接口
````java
@FeignClient(value = "admin-client",fallback = HiServiceImpl.class)
public interface HiService {
    @RequestMapping(value = "/hello")
    String sayLikeClientOne(@RequestParam(value = "name") String name);
}
````
- HiServerImpl 实现类
````java
@Component
public class HiServiceImpl implements HiService {
    @Override
    public String sayLikeClientOne(String name) {
        return "sorry," + name;
    }
}
````

#### ④controller层
````java
@RestController
public class HiController {
    @Autowired
    HiService hiService; // 编译器报错，无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错

    @RequestMapping("/hello")
    public String like(@RequestParam(value = "name", defaultValue = "flyxiaozhu") String name) {
        return hiService.sayLikeClientOne(name);
    }
}
````

### 四、网关 zuul
#### ①依赖
````java
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>
````
#### ②启动类
````java
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ServerZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerZuulApplication.class, args);
	}
}
````
#### ②过滤
````java
@Component
public class MyFilter extends ZuulFilter{

    private static Logger log = LoggerFactory.getLogger(MyFilter.class);

    // 返回一个字符串代表过滤器的类型
    // 生命周期：
    // pre：路由之前
    // routing：路由之时
    // post： 路由之后
    // error：发送错误调用
    @Override
    public String filterType() {
        return "pre";
    }

    // 过滤的顺序
    @Override
    public int filterOrder() {
        return 0;
    }

    // 这里可以写逻辑判断，是否要过滤，本文true,永远过滤。
    @Override
    public boolean shouldFilter() {
        return true;
    }

    // 过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问。
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info(String.format("%s >>> %s",request.getMethod(),request.getRequestURL().toString()));

        Object accessTonken = request.getParameter("token");
        if(accessTonken == null){
            log.warn("token is empty");

            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);

            try{
                ctx.getResponse().getWriter().write("token is empty");
            }catch (Exception e){
                log.error(e.getMessage());
            }
            return null;
        }
        log.info("ok");
        return null;
    }
}
````

### 五、分布式配置中心
> config-server
#### ①依赖
````java
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
````
#### ②启动类
springboot工程的启动application类上加@EnableConfigServer注解
````java
@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}
````
#### ③集群配置
- application.yml
````java
spring:
  application:
    name: config-server
  profiles:
    active: config1
info:
  version: @project.version@
````
- application-config1.yml
````java
server:
  port: 7002
spring:
  profiles: config1
#   git 仓库配置
  cloud:
    config:
      server:
        git:
          uri: https://github.com/fiyxiaozhu/SpringCloudConfig # 配置git仓库的地址
          search-paths: respo  # git仓库地址下的相对地址，可以配置多个，用,分割。
          username:  # git仓库的用户和密码
          password:
      label: master # 分支
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7000/eureka/
    registry-fetch-interval-seconds: 5 #表示eureka client间隔多久去拉取服务注册信息，默认为30秒
  instance:
    lease-renewal-interval-in-seconds: 10 # 心跳时间，即服务续约间隔时间（缺省为30s）
    lease-expiration-duration-in-seconds: 30 # 发呆时间，即服务续约到期时间（缺省为90s）
    health-check-url-path: /actuator/health #健康监测相对路径
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    prefer-ip-address: true #是否显示IP地址
management:
  endpoints:
    web:
      exposure:
        include: "*" #暴露所有 endpoints
  endpoint:
    health:
      show-details: always
````
- application-config2.yml
````java
server:
  port: 7003
spring:
  profiles: config2
#   git 仓库配置
  cloud:
    config:
      server:
        git:
          uri: https://github.com/fiyxiaozhu/SpringCloudConfig # 配置git仓库的地址
          search-paths: respo  # git仓库地址下的相对地址，可以配置多个，用,分割。
          username:  # git仓库的用户和密码
          password:
      label: master # 分支
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7000/eureka/
    registry-fetch-interval-seconds: 5 #表示eureka client间隔多久去拉取服务注册信息，默认为30秒
  instance:
    lease-renewal-interval-in-seconds: 10 # 心跳时间，即服务续约间隔时间（缺省为30s）
    lease-expiration-duration-in-seconds: 30 # 发呆时间，即服务续约到期时间（缺省为90s）
    health-check-url-path: /actuator/health #健康监测相对路径
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    prefer-ip-address: true #是否显示IP地址
management:
  endpoints:
    web:
      exposure:
        include: "*" #暴露所有 endpoints
  endpoint:
    health:
      show-details: always
````
> config-client

#### ①依赖
````java
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
````
#### ②启动类
springboot工程的启动application类上加@EnableConfigServer注解
````java

````
#### ③集群配置
- application.yml
````java
server:
  port: 7010
spring:
  application:
    name: admin-client
info:
  version: @project.version@ # 显示版本信息
````
- bootstrap.yml
````java
# git仓库配置
spring:
  cloud:
    config:
      label: master
      profile: dev
      discovery:
        enabled: true
        service-id: config-server
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7000/eureka/
````

---
> 配置仓库
![img](doc/config.png)

### 五、spring-boot-admin
#### ①依赖
````java
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-server</artifactId>
</dependency>

<!--有spring-boot-admin-starter-server依赖，所以不需要该依赖-->
<!--<dependency>-->
  <!--<groupId>org.springframework.boot</groupId>-->
  <!--<artifactId>spring-boot-starter-actuator</artifactId>-->
<!--</dependency>-->

<dependency>
  <groupId>de.codecentric</groupId>
  <artifactId>spring-boot-admin-server-ui</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!--增加安全控制的依赖-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!--JMX-bean 管理所需依赖-->
<dependency>
  <groupId>org.jolokia</groupId>
  <artifactId>jolokia-core</artifactId>
</dependency>
````
#### ②启动类
````java
@SpringBootApplication
@EnableAdminServer // 开启监控功能
@EnableEurekaClient //注册到 Eureka
@RefreshScope // 开启配置自动刷新功能
public class AdminServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminServerApplication.class, args);
	}

    @Profile("insecure") // 测试时搭配@ActiveProfiles("insecure")使用 也可以在配置文件中 spring.profiles.active 配置
    @Configuration // 表明是个配置类
//    @EnableWebSecurity
	public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
        }
    }
//
    @Profile("secure")
    @Configuration
//    @EnableWebSecurity // 注解开启Spring Security的功能
    public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
        private final String adminContextPath;

        public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            successHandler.setTargetUrlParameter("redirectTo");

            http.authorizeRequests() //定义哪些URL需要被保护、哪些不需要被保护
                    .antMatchers(adminContextPath + "/assets/**").permitAll()
                    .antMatchers(adminContextPath + "/login").permitAll()
                    .anyRequest().authenticated() // 表示其他的请求都必须要有权限认证
                    .and()
                    .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler)
                    .and()
                    .logout().logoutUrl(adminContextPath + "/logout")
                    .and()
                    .httpBasic()
                    .and()
                    .csrf().disable();
        }
    }
}
````

#### ④配置
- application.yml
````java
server:
  port: 7001
spring:
  application:
    name: admin-server
#  mail:
#    host: smtp.qq.com
#    username: 943569813@qq.com
#    password: sdrqyfbpdibkbdia # POP3/SMTP服务 授权码
#    properties:
#      mail:
#        smtp:
#          auth: true # 开启用户身份验证
#    default-encoding: UTF-8
#    port: 465
#  boot:
#    admin:
#      notify:
#        mail:
#          to: 1647622369@qq.com # 发送给谁
#          from: 943569813@qq.com # 谁发出去的
#  rabbitmq: # rabbitmq配置
#    host: localhost
#    port: 5672
#    username: guest
#    password: guest
#  cloud:
#    bus:
#      enabled: true
#      trace:
#        enabled: true # 开启消息跟踪
info:
  version: @project.version@ # 显示版本信息
````
- bootstrap.yml
````java
# git仓库配置
spring:
  cloud:
    config:
      label: master
      profile: dev
      discovery:
        enabled: true # 从配置中心读取文件
        service-id: config-server # 配置中心的serviceId，即服务名
      fail-fast: true # 获取文件失败重试
      retry:
        initial-interval: 1000 # 最初重试间隔为 1000 毫秒
        max-attempts: 6 # 最多重试 6 次
        max-interval: 2000 # 最长重试间隔为 2000 毫秒
        multiplier: 1.1 # 每次重试失败后，重试间隔所增加的倍数
eureka:
  client:
    service-url:
      defaultZone: http://flyxiaozhu:1120zh.+@localhost:7000/eureka/ #注册地址 如果服务器端设置安全认证了 客户端在在配置defaultZone的时候需要带上用户名、密码
````
- admin-server-dev.yml
````java
spring:
  profiles:
    active: secure # 配置选择 搭配 @Profile("insecure") 使用
  security:
    user:
      name: flyxiaozhu # 设置登陆的用户名和密码 client为单纯的spring-boot程序
      password: 1120zh.+
eureka:
  instance:
    lease-renewal-interval-in-seconds: 10 # 心跳时间，即服务续约间隔时间（缺省为30s）
    lease-expiration-duration-in-seconds: 30 # 发呆时间，即服务续约到期时间（缺省为90s）
    health-check-url-path: /actuator/health #健康监测相对路径
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    prefer-ip-address: true #是否显示IP地址
    metadata-map: # 注册给eureka的时候告诉eureka自己的密码
      user:
        name: flyxiaozhu # client为spring-cloud程序
        password: 1120zh.+
  client:
    registry-fetch-interval-seconds: 5 #表示eureka client间隔多久去拉取服务注册信息，默认为30秒
management:
  endpoints:
    web:
      exposure:
        include: "*" #暴露所有 endpoints
  endpoint:
    health:
      show-details: always
````
#### ⑤运行结果
- login
![img](doc/login.png)
- application
![img](doc/admin-server.png)
- 服务上下线日志
![img](doc/journal.png)
- 服务信息
![img](doc/info.png)

#### ⑥注意
- 使用 jvm 需要该依赖
````java
<!--JMX-bean 管理所需依赖-->
<dependency>
  <groupId>org.jolokia</groupId>
  <artifactId>jolokia-core</artifactId>
</dependency>
````
- 查看服务详细信息 
````java
<!--暴露自身信息端点依赖-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
````

- 配置
````java
management:
  endpoints:
    web:
      exposure:
        include: "*"  #暴露所有 endpoints
  endpoint:
    health:
      show-details: always
````

- 控制服务日志级别，需要配置logback
resources目录下添加logback.xml
````java
<configuration>
  <include resource="org/springframework/boot/logging/logback/base.xml"/>
  <jmxConfigurator/>
</configuration>
````


### 六、读取git配置超时和重试
#### ①依赖
````java
<!--获取git文件失败重启的依赖-->
<dependency>
  <groupId>org.springframework.retry</groupId>
  <artifactId>spring-retry</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
````
#### ②配置
````java
# git仓库配置
spring:
  cloud:
    config:
      retry:
        initial-interval: 1000 # 最初重试间隔为 1000 毫秒
        max-attempts: 6 # 最多重试 6 次
        max-interval: 2000 # 最长重试间隔为 2000 毫秒
        multiplier: 1.1 # 每次重试失败后，重试间隔所增加的倍数
````

### 七、config自动刷新
#### ①依赖
>需要安装rabbitmq
````java
<!--自动刷新配置-->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
````
#### ②启动类
启动类中加入@RefreshScope注解
#### ③配置
````java
spring:
  rabbitmq: # rabbitmq配置
    host: localhost
    port: 5672
    username: guest
   password: guest
  cloud:
    bus:
      enabled: true
      trace:
        enabled: true # 开启消息跟踪
````
#### ④使用
> 当git仓库里面的配置发生改变，post 访问 http://localhost:7001/actuator/bus-refresh  
则该服务器会重新去git仓库获取配置

### 八、服务挂掉重启











