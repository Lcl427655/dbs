package com.cn.dbs.service.impl;

import com.cn.dbs.dao.FbpEmployeesDao;
import com.cn.dbs.entity.FbpEmployees;
import com.cn.dbs.mapper.FbpEmployeesMapper;
import com.cn.dbs.service.FbpEmployeesService;
import com.cn.dbs.service.FbpProxySynLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FbpEmployeesServiceImpl implements FbpEmployeesService {

    @Autowired
    private FbpEmployeesDao fbpEmployeesDao;

    @Autowired
    private FbpProxySynLogService fbpProxySynLogService;

    @Autowired
    private FbpEmployeesMapper fbpEmployeesMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED ,
            rollbackFor = Exception.class)
    public void updateEmployees() {
        try {
            fbpEmployeesDao.updateEmployees();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("运行异常");
        } finally {
            fbpProxySynLogService.insertLog("112233","输入参数","输出参数");
        }
    }

    @Override
    public List<FbpEmployees> getFbpEmployeesList() {
        List<FbpEmployees> list = fbpEmployeesMapper.getFbpEmployeesList();
        System.out.println("list = " + list);
        return list;
    }

    @Override
    public Integer getEmployeesCount() {
        return fbpEmployeesDao.getEmployeesCount();
    }
}
