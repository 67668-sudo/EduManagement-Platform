package org.example.service;

import org.example.pojo.JobOption;
import org.example.pojo.StudentOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    public JobOption getEmpJobData();
    public List<Map<String, Object>> getEmpGenderData();

    List<Map<String, Object>> getStudentDegreeData();

    StudentOption getStudentCountData();
}
