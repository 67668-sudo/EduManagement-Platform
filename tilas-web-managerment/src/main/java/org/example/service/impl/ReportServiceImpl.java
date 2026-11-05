package org.example.service.impl;

import org.example.mapper.EmpMapper;
import org.example.mapper.StudentMapper;
import org.example.pojo.JobOption;
import org.example.pojo.StudentOption;
import org.example.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public JobOption getEmpJobData() {
        List<Map<String, Object>> list = empMapper.countEmpJobData();

        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).collect(Collectors.toList());
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("job_count")).collect(Collectors.toList());
        return new JobOption(jobList, dataList);
    }

    @Override
    public List<Map<String, Object>> getEmpGenderData() {
       return empMapper.countEmpGenderData();
    }

    @Override
    public List<Map<String, Object>> getStudentDegreeData() {
        return studentMapper.countStudentDegreeData();
    }

    @Override
    public StudentOption getStudentCountData() {
        List<Map<String, Object>> list = studentMapper.countStudentCountData();
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("name")).collect(Collectors.toList());
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("value")).collect(Collectors.toList());
        return new StudentOption(clazzList, dataList);
    }


}
