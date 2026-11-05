package org.example.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.mapper.LogMapper;
import org.example.pojo.OperateLog;
import org.example.pojo.PageResult;
import org.example.service.LogService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;
    @Override
    public PageResult<OperateLog> getPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OperateLog> logs = logMapper.getAllLogs();
        Page<OperateLog> page = (Page<OperateLog>) logs;
        return new PageResult<>(page.getTotal(), page.getResult());
    }
}
