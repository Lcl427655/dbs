package com.cn.dbs.service.impl;

import com.cn.dbs.dao.FbpProxySynLogDao;
import com.cn.dbs.service.FbpProxySynLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FbpProxySynLogServiceImpl implements FbpProxySynLogService {

    @Autowired
    private FbpProxySynLogDao fbpProxySynLogDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW , noRollbackFor = Exception.class)
    public int insertLog(String priKey, String input, String output) {
        return fbpProxySynLogDao.insertLog(priKey,input,output);
    }
}
