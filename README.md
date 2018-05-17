# spring-boot-api-project-seed

利用本项目可以快速生成一个基于Spring Boot的Restful Api项目

如果你想构建一个中小型项目，利用本项目无疑是非常快速的

如果你想构建一个大型项目，本项目也是一个不错的起步

本项目是在[spring-boot-api-project-seed](https://github.com/lihengming/spring-boot-api-project-seed)基础改造而来，主要是原项目
有不适合我需求的地方，当然我改造的项目也不一定符合你的需求，重要的是大家可以利用它改造成适合自己的一个种子项目


## 特性
- 提供Restful Api，HttpCode为2xx表示处理成功
处理成功时，返回的HttpCode为2xx，返回值可以直接从body中读取
处理失败时，返回的HttpCode为4xx(表示客户端错误)或5xx(表示服务端错误)，错误信息从body中读取

- 统一异常处理

- 打印请求日志

- 使用Lombok简化代码

- 使用Druid数据库连接池

## 使用的技术

- Spring Boot 2.x
- Mybatis
- Druid
- MyBatis PageHelper分页插件
- MyBatisb通用Mapper插件
- Lombok
- FastJson

## 如何使用

1. clone本项目
2. 修改test/java/com/company/project/GeneratorConstant中配置的参数，主要是JDBC相关的参数和BASE_PACKAGE
3. 修改main/java/com/company/project/Application中注解MapperScan的basePackages参数
4. 修改application-default.yml中的配置
5. 修改其他你觉得要修改的地方
6. 使用test/java/com/company/project/CodeGenerator生成代码
