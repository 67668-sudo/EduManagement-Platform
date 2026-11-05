package org.example;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

public class Demo {
    public static void main(String[] args) throws Exception {
        // 1. Endpoint 以华东1（杭州）为例，请按实际 Region 填写
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";

        // 2. 从环境变量中获取访问凭证（推荐）
        // 运行前请确保已设置 OSS_ACCESS_KEY_ID 和 OSS_ACCESS_KEY_SECRET 环境变量
        EnvironmentVariableCredentialsProvider credentialsProvider =
                CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 3. 填写 Bucket 名称和 Object 完整路径（路径中不要包含 Bucket 名称）
        String bucketName = "java-ai-web-final";
        String objectName = "001.jpg";
        String region = "cn-beijing";

        // 4. 创建 OSSClient 实例（使用 V4 签名）
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            // 5. 上传字符串内容（实际使用时，可将 ByteArrayInputStream 替换为 file.getInputStream()）
            File file = new File("C:\\Users\\liuG\\Desktop\\sumanli_fengmian.png");
            // 读取文件内容，并直接作为上传的输入
            byte[] content = Files.readAllBytes(file.toPath());
            PutObjectResult result = ossClient.putObject(
                    bucketName,
                    objectName,
                    new ByteArrayInputStream(content)
            );
            System.out.println("上传成功，VersionId: " + result.getVersionId());
        } catch (OSSException oe) {
            System.out.println("OSS 服务端异常: " + oe.getErrorMessage());
        } finally {
            // 6. 释放资源，关闭 OSSClient
            ossClient.shutdown();
        }
    }
}