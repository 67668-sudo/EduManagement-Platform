package org.example.service;

import org.example.pojo.OperateLog;
import org.example.pojo.PageResult;

public interface LogService {
    PageResult<OperateLog> getPage(int pageNum, int pageSize);
}
