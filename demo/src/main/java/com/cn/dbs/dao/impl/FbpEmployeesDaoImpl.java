package com.cn.dbs.dao.impl;

import com.cn.dbs.dao.FbpEmployeesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FbpEmployeesDaoImpl implements FbpEmployeesDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer getEmployeesCount() {
        String sql = "select count(*) from fbp_employees s where s.employee_number = '01024201'";
        Integer i = jdbcTemplate.queryForObject(sql, Integer.class);
        return i;
    }

    @Override
    public void updateEmployees() {
        String sql = "update fbp_employees s set s.created_by = 111 where s.enabled_flag = 'N' and s.employee_number = '00952357'";
        int i = jdbcTemplate.update(sql);
        System.out.println("i = " + i);
        int ii = 1/0;
    }
}
