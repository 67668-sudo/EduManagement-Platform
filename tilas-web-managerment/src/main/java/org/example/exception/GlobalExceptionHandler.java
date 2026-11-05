package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error("程序出错了，完蛋了：", e);
        return Result.error("请联系管理员你刘哥");
    }
    @ExceptionHandler({NotExistEmpException.class, ExistStudentException.class, RepeatNoException.class})
    public Result handleBizException(RuntimeException e) {
        return Result.error(e.getMessage());
    }

//    @ExceptionHandler
//    public Result handleNotExistEmpException(NotExistEmpException e){
//        log.error("程序出错了，完蛋了：", e);
//        return Result.error(e.getMessage());
//    }

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e){
        log.error("冲突l ：", e);
        String message = e.getMessage();
        int i = message.indexOf("Duplicate entry");
        String errmsg = message.substring(i);
        String[] arr = errmsg.split(" ");
        return Result.error(arr[2]+"已经存在了");
    }
}
