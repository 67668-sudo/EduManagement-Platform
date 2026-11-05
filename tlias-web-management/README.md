# 🏫 EduManagement 主项目

> 业务后端 + 部署说明书  
> 项目总览、模块划分、技术栈 → 看[仓库根目录的 README](../README.md) 👀

## 🧰 环境要求

| 依赖 | 版本 |
| --- | --- |
| ☕ JDK | 25 |
| 📦 Maven | 3.9+ |
| 🗄️ MySQL | 8.0+ |
| 🐳 Docker / Compose | 可选，用于容器化部署 |

## 🏃 一、本地运行

### 1. 🗄️ 准备数据库

创建库并导入脚本（脚本位于仓库根目录的 `sql/tlias.sql`）：

```sql
CREATE DATABASE tlias DEFAULT CHARACTER SET utf8mb4;
```

```bash
mysql -u root -p tlias < ../sql/tlias.sql
```

### 2. ⚙️ 配置数据库连接

修改 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tlias?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: your_username
    password: your_password
```

### 3. ☁️ 配置阿里云 OSS（可选）

文件上传功能依赖 OSS，需设置环境变量：

```bash
# Windows PowerShell
$env:OSS_ACCESS_KEY_ID="your_access_key_id"
$env:OSS_ACCESS_KEY_SECRET="your_access_key_secret"

# Linux / macOS
export OSS_ACCESS_KEY_ID=your_access_key_id
export OSS_ACCESS_KEY_SECRET=your_access_key_secret
```

不配置时其余接口不受影响，仅 `/upload` 不可用 🙈

### 4. ▶️ 启动

> ⚠️ 首次运行前，请先确保同仓库下的两个 starter 模块已安装到本地仓库（见根 README 的「构建」一节）。

```bash
mvn spring-boot:run
```

默认端口 `8080`。

### 5. 🔑 登录获取令牌

```http
POST /login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

返回体中的 `data` 即 JWT 令牌 🎫 除 `/login` 外，其余接口都需在请求头中携带：

```
token: <jwt>
```

## 📦 二、打包

```bash
mvn clean package -DskipTests
```

产物：`target/tlias-web-management-0.0.1-SNAPSHOT.jar`

```bash
java -jar target/tlias-web-management-0.0.1-SNAPSHOT.jar
```

## 🐳 三、Docker Compose 部署

### 📁 目录约定

```
docker/
├── Dockerfile
├── docker-compose.yml
└── nginx.conf
```

`docker-compose.yml` 的构建上下文设为仓库根目录，因此 Dockerfile 中可以直接访问三个模块。

### 🐳 Dockerfile

```dockerfile
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
```

> 💡 若拉取不到 `25` 对应的镜像标签，请换成你本机的 JDK 大版本（如 `21`），并同步修改 `pom.xml` 中的 `java.version`。

### 🧩 docker-compose.yml

```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: tlias-mysql
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD:-123456}
      MYSQL_DATABASE: tlias
      TZ: Asia/Shanghai
    volumes:
      - ../sql:/docker-entrypoint-initdb.d   # 首次启动自动导入建表脚本
      - mysql-data:/var/lib/mysql
    ports:
      - "3306:3306"
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 5s
      timeout: 3s
      retries: 10

  app:
    build:
      context: ..
      dockerfile: docker/Dockerfile
    container_name: tlias-app
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/tlias?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD:-123456}
      OSS_ACCESS_KEY_ID: ${OSS_ACCESS_KEY_ID}
      OSS_ACCESS_KEY_SECRET: ${OSS_ACCESS_KEY_SECRET}
    ports:
      - "8080:8080"

volumes:
  mysql-data:
```

### 🚢 启动

```bash
cd docker
docker compose up -d --build
docker compose logs -f app     # 查看启动日志
```

停止并清理：

```bash
docker compose down          # 保留数据
docker compose down -v       # 连数据卷一起删除
```

## 🐧 四、Linux 手动部署（不使用 Docker）

```bash
# 上传 jar 到服务器
scp target/tlias-web-management-0.0.1-SNAPSHOT.jar root@<你的服务器IP>:/opt/app/

# 服务器上启动
nohup java -jar /opt/app/tlias-web-management-0.0.1-SNAPSHOT.jar \
  --spring.datasource.password=<数据库密码> \
  > /opt/app/app.log 2>&1 &
```

Nginx 反向代理示例：

```nginx
server {
    listen 80;
    server_name <你的域名或IP>;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        client_max_body_size 10m;
    }
}
```

## 🩺 五、常见问题

**1. 启动报 `Access denied for user`**  
数据库用户名或密码不对。Docker 部署时用 `SPRING_DATASOURCE_*` 环境变量覆盖，无需改 `application.yml`。

**2. 容器内连不上 MySQL**  
`application.yml` 里的 `localhost` 在容器内指向应用自己。用 Compose 部署时，把地址改成服务名 `mysql`（已在上面用环境变量覆盖）。

**3. 接口一直返回 401**  
请求头缺失 `token`，或令牌已过期（默认 12 小时）。重新调用 `/login` 获取 🎫

**4. 文件上传报错**  
检查 `OSS_ACCESS_KEY_ID` / `OSS_ACCESS_KEY_SECRET` 是否已注入容器环境变量。

**5. 中文时间差 8 小时**  
确认 JVM 时区为 `Asia/Shanghai`（Dockerfile 中已设置 `TZ`）。
