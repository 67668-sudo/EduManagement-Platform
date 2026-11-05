# 🚀 EduManagement 智能学习辅助系统

> 一个基于 Spring Boot 4 + Vue 3 的后台管理系统，外加一个手写的阿里云 OSS Spring Boot Starter 🧩
>
> 后端接口 + 前端页面 + 自定义组件 + 容器化部署，全在这一个仓库里 🙌
>
> ℹ️ **后端代码均为本人独立实现**；前端页面由 AI 辅助生成，仅用于演示接口的完整调用链路。

## 📖 这是什么

一套面向教师的教务管理后台：能管部门、管员工、管班级、管学生、看统计报表、传文件，还带登录认证和操作日志。

仓库里有三个主角：

- 🏫 **主项目**：完整的业务后端，能直接跑起来
- 🖥️ **前端页面**：AI 辅助生成的 Vue 3 管理界面，用于演示接口调用
- 📦 **自定义 Starter**：自己按 Spring Boot 规范封装的 OSS 上传组件，主项目直接引依赖就能用

## 🗂️ 仓库结构

| 目录 | 说明 |
| --- | --- |
| `tlias-web-management/` | 🏫 主项目：部门、员工、班级、学生、报表、文件上传、登录认证、操作日志 |
| `frontend/` | 🖥️ 前端页面（Vue 3 构建产物），含登录页、业务页与图表 |
| `aliyun-oss-spring-boot-autoconfigure/` | 📦 自定义 Starter 的自动配置模块 |
| `aliyun-oss-boot-starter/` | 🎁 自定义 Starter 的门面模块 |
| `sql/` | 🗄️ 数据库脚本 |
| `docker/` | 🐳 部署配置：`Dockerfile`、`docker-compose.yml`、`nginx.conf` |

## 🛠️ 技术栈

| 分类 | 技术 |
| --- | --- |
| 语言 / 框架 | ☕ Java 25、Spring Boot 4、Spring MVC |
| 持久层 | 🗃️ MyBatis、PageHelper 分页插件、MySQL 8 |
| 认证 | 🔐 JWT（jjwt）、HandlerInterceptor、ThreadLocal |
| 切面 | ✂️ Spring AOP + 自定义注解 |
| 日志 | 📝 Logback、操作日志落库 |
| 组件封装 | 🧩 自定义 Spring Boot Starter（阿里云 OSS） |
| 前端（演示用） | 🖥️ Vue 3、Vue Router、Pinia、Element Plus、Axios、ECharts、Vite（🤖 AI 辅助生成） |
| 部署与运维 | 🐧 Linux、🐳 Docker、Docker Compose、Nginx |

## ✨ 业务模块

- 🏢 **部门管理**：列表查询、新增、修改、删除
- 👨‍💼 **员工管理**：分页 + 条件查询、新增（含工作经历批量保存）、修改、批量删除、员工下拉列表
- 🏫 **班级管理**：查询、新增、修改、删除（删除前校验班级下是否仍有学生）
- 🎓 **学生管理**：分页查询、新增、修改、删除、违纪扣分
- 📊 **报表统计**：员工职位分布、员工性别分布、班级学生人数（ECharts 图表）
- ☁️ **文件上传**：上传至阿里云 OSS，返回可访问 URL
- 🔐 **登录认证**：登录签发 JWT，拦截器校验令牌，`ThreadLocal` 保存当前登录人
- 📝 **操作日志**：`@LogOperation` 注解 + AOP 环绕通知，自动记录操作人、类名、方法名、参数、返回值与耗时
- 🧯 **统一响应与异常**：统一 `Result` 返回体、全局异常处理器、三个自定义业务异常

## 💡 核心亮点

1. 🔐 **无状态登录认证**：基于 JWT 实现，配合拦截器保障接口安全，令牌失效统一返回 401。
2. 📝 **操作日志审计**：基于 AOP 环绕通知拦截标注了 `@LogOperation` 的方法，自动记录操作人、耗时等字段并落库。
3. 🧩 **自定义组件封装**：按 Spring Boot 自动配置规范实现了阿里云 OSS 上传组件，拆分为 `autoconfigure` 与 `starter` 两个模块，主项目只需引入依赖即可使用。
4. 🖥️ **前后端分离部署**：前端（AI 辅助生成）由 Nginx 托管在 80 端口，后端运行在 8080，通过反向代理打通，无需处理跨域。
5. 🐳 **容器化部署**：使用 Docker Compose 一键编排前端、后端与 MySQL 三个服务。

## 🚀 快速体验

用 Docker Compose 起全套服务后：

| 地址 | 说明 |
| --- | --- |
| `http://localhost/login` | 🖥️ 前端登录页 |
| `http://localhost:8080` | 🔌 后端接口（除 `/login` 外均需请求头携带 `token`） |

> 🔑 登录账号需与数据库 `emp` 表中的 `username` / `password` 一致。

## 🔨 构建

三个后端模块存在依赖关系，需要按顺序构建（别跳步，不然会找不到依赖 🐟）：

```bash
# 1️⃣ 自动配置模块
cd aliyun-oss-spring-boot-autoconfigure
mvn clean install -DskipTests

# 2️⃣ 门面模块
cd ../aliyun-oss-boot-starter
mvn clean install -DskipTests

# 3️⃣ 主项目
cd ../tlias-web-management
mvn clean package -DskipTests
```

🖥️ 前端 `frontend/dist` 是已构建好的产物，无需再次打包，直接由 Nginx 托管即可。

## 📚 文档导航

- 🏃 主项目的本地运行与部署说明：[`tlias-web-management/README.md`](tlias-web-management/README.md)
- 🧩 自定义 Starter 的使用说明：[`aliyun-oss-boot-starter/README.md`](aliyun-oss-boot-starter/README.md)
- 🐳 部署配置：[`docker/`](docker/)
- 🗄️ 数据库脚本：[`sql/tlias.sql`](sql/tlias.sql)
