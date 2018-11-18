package com.cn.dbs.service;

import com.cn.dbs.entity.FbpEmployees;

import java.util.List;

public interface FbpEmployeesService {
    Integer getEmployeesCount();

    void updateEmployees();

    List<FbpEmployees> getFbpEmployeesList();
}
