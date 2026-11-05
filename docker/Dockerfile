# ---------- 构建阶段 ----------
FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /build

# 先安装自定义 starter，主项目依赖它
COPY aliyun-oss-spring-boot-autoconfigure ./aliyun-oss-spring-boot-autoconfigure
COPY aliyun-oss-boot-starter ./aliyun-oss-boot-starter
COPY tlias-web-management ./tlias-web-management

RUN cd aliyun-oss-spring-boot-autoconfigure && mvn -q clean install -DskipTests \
 && cd ../aliyun-oss-boot-starter && mvn -q clean install -DskipTests \
 && cd ../tlias-web-management && mvn -q clean package -DskipTests

# ---------- 运行阶段 ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=builder /build/tlias-web-management/target/*.jar app.jar

ENV TZ=Asia/Shanghai
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]