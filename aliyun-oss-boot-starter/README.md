# 📦 aliyun-oss-boot-starter

> 一个手写的阿里云 OSS 上传 Spring Boot Starter 🧩
>
> 引入依赖 + 写三行配置，就能在任意 Spring Boot 项目里注入 `AliyunOSSOperator` 直接传文件。

## 📖 这是什么

这个组件把「创建 OSSClient → 上传 → 拼访问 URL → 关闭客户端」这套重复代码封装了起来。

它拆成两个模块：

| 模块 | 职责 |
| --- | --- |
| `aliyun-oss-boot-starter` 🎁 | 门面模块，**使用者只需要引入这一个** |
| `aliyun-oss-spring-boot-autoconfigure` ⚙️ | 自动配置模块，包含 `AliyunOSSOperator`、`AliyunOSSProperties` 和自动配置类 |

## ⚙️ 工作原理

1. `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 里声明了自动配置类，Spring Boot 启动时会自动加载；
2. `AliyunOSSAutoConfiguration` 通过 `@EnableConfigurationProperties` 绑定 `aliyun.oss.*` 配置，并注册 `AliyunOSSOperator` 这个 Bean；
3. Bean 上加了 `@ConditionalOnMissingBean`，**如果你自己定义了一个同名 Bean，就会用你的**，不会被覆盖。

## 🚀 快速开始

### 1. ☁️ 阿里云控制台准备

1. 开通 OSS 服务，进入控制台
2. 创建一个 **Bucket**，记下：
   - Bucket 名称，例如 `java-ai-web-final`
   - 地域，例如 `华北2（北京）` → 对应 `cn-beijing`
3. 创建一个 **RAM 用户**（不要用主账号 AccessKey），授予 `AliyunOSSFullAccess` 或只针对该 Bucket 的读写权限
4. 为该用户创建 AccessKey，记下 **AccessKey ID** 和 **AccessKey Secret**

### 2. 📥 引入依赖

先把两个模块装进本地仓库（在仓库根目录按顺序执行）：

```bash
cd aliyun-oss-spring-boot-autoconfigure && mvn clean install -DskipTests
cd ../aliyun-oss-boot-starter && mvn clean install -DskipTests
```

然后在业务项目中引入：

```xml
<dependency>
    <groupId>org.aliyun.oss</groupId>
    <artifactId>aliyun-oss-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 3. ⚙️ 配置 application.yml

```yaml
aliyun:
  oss:
    endpoint: https://oss-cn-beijing.aliyuncs.com
    bucket-name: java-ai-web-final
    region: cn-beijing
```

### 4. 🔑 配置 AccessKey

AccessKey **不要写进配置文件**，用环境变量：

```bash
# Windows PowerShell
$env:OSS_ACCESS_KEY_ID="your_access_key_id"
$env:OSS_ACCESS_KEY_SECRET="your_access_key_secret"

# Linux / macOS
export OSS_ACCESS_KEY_ID=your_access_key_id
export OSS_ACCESS_KEY_SECRET=your_access_key_secret
```

> 🐳 Docker 部署时，在 `docker-compose.yml` 的 `environment` 里传入这两个变量即可。

### 5. 🎯 使用

```java
@RestController
public class UploadController {

    private final AliyunOSSOperator aliyunOSSOperator;

    public UploadController(AliyunOSSOperator aliyunOSSOperator) {
        this.aliyunOSSOperator = aliyunOSSOperator;
    }

    @PostMapping("/upload")
    public String upload(MultipartFile file) throws Exception {
        return aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
    }
}
```

`upload` 的入参是文件字节数组和原始文件名，**返回可直接访问的文件 URL**。

对象存储路径会自动按 `yyyy/MM/uuid.扩展名` 生成，例如：

```
https://java-ai-web-final.oss-cn-beijing.aliyuncs.com/2026/11/8f3c1a2e-....png
```

## 🔧 配置项

| 配置 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `aliyun.oss.endpoint` | String | ✅ | OSS 访问域名，形如 `https://oss-cn-beijing.aliyuncs.com` |
| `aliyun.oss.bucket-name` | String | ✅ | Bucket 名称 |
| `aliyun.oss.region` | String | ✅ | 地域标识，如 `cn-beijing`（使用 V4 签名时必填） |
| `OSS_ACCESS_KEY_ID` | 环境变量 | ✅ | 阿里云 AccessKey ID |
| `OSS_ACCESS_KEY_SECRET` | 环境变量 | ✅ | 阿里云 AccessKey Secret |

## 🛠️ 自定义覆盖

如果你不想用默认实现，只需要自己定义一个 `AliyunOSSOperator` Bean：

```java
@Configuration
public class OssConfig {

    @Bean
    public AliyunOSSOperator myOssOperator(AliyunOSSProperties properties) {
        return new AliyunOSSOperator(properties);
    }
}
```

自动配置检测到已有 Bean 时就不会再注册，这就是 `@ConditionalOnMissingBean` 的作用。

## 🩺 常见问题

**1. 启动报 `NoSuchBeanDefinitionException: AliyunOSSOperator`**

依赖没引入成功，或者自动配置没生效。先确认 `autoconfigure` 模块已经 `mvn install` 到本地仓库，再检查 `target/classes/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 是否存在于自己的 jar 里。

**2. 上传报 `The bucket you access does not belong to you`**

`bucket-name` 和 `region` 不匹配，或者 AccessKey 没有该 Bucket 的权限。

**3. 上传成功但 URL 打不开**

新建的 Bucket 默认是 **私有读写**，直接把 URL 贴到浏览器会返回 `AccessDenied`。两种解决方式：

- 在控制台把 Bucket 读权限改为**公共读**（仅适合公开素材）
- 保持私有，改成返回**签名 URL**（`ossClient.generatePresignedUrl`）

**4. 报 `AccessDenied` / `InvalidAccessKeyId`**

环境变量没生效。注意 IDE 里配置的环境变量需要在重启 IDE 后才会被读取。

**5. 上传报 `RequestTimeTooSkewed`**

服务器时间不准确，校准系统时间即可。
