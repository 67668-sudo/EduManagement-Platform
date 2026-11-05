package org.example.controller;

import com.aliyun.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class UploadController {

//    @PostMapping("/upload")//存在本地磁盘
//    public Result upload(String name, Integer age, MultipartFile file) throws IOException {
//        log.info("接受参数{},{},{}", name, age, file);
//        String newFileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        file.transferTo(new File("D:\\develop\\web-ai\\first_ai\\tilas-web-managerment\\src\\main\\files\\" + newFileName));
//        return Result.success();
//    }
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传{}", file.getOriginalFilename());
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        return Result.success(url);
    }

}
