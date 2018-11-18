package com.cn.dbs.dao.impl;

import com.cn.dbs.dao.FbpProxySynLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FbpProxySynLogDaoImpl implements FbpProxySynLogDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insertLog(String priKey, String input, String output) {
        String sql = "insert into FBP.FBP_PROXY_SYN_LOG" +
                "(SYN_ID,LAST_UPDATE_DATE,LAST_UPDATED_BY,CREATION_DATE," +
                "CREATED_BY,LAST_UPDATE_LOGIN,ENABLED_FLAG,PRI_KEY,INPUT,OUTPUT)" +
                " values(FBP.FBP_PROXY_SYN_LOG_S.NEXTVAL,sysdate,1,sysdate," +
                "1,1,'Y',?,?,?)";
        int i = jdbcTemplate.update(sql, priKey, input, output);
        return i;
    }
}
