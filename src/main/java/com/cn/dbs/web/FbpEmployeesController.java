package com.cn.dbs.web;

import com.cn.dbs.service.FbpEmployeesService;
import com.cn.my.config.MyConfig;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FbpEmployeesController {

    private final static Logger logger = LoggerFactory.getLogger(FbpEmployeesController.class);

    @Autowired
    private MyConfig myConfig;

    @Autowired
    private FbpEmployeesService fbpEmployeesService;

    @GetMapping(value = "/user")
    @ResponseBody
    public String getUser(){
        logger.debug("======getUser============debug==============");
        logger.info("======getUser============info==============");
        logger.error("======getUser============error==============");
        logger.warn("======getUser============warn==============");
        return "user";
    }

    @GetMapping("/show/{id}")
    @ResponseBody
    public String show(@PathVariable("id") Integer id){
        System.out.println("id = " + id);
        //读取自定义配置
        System.out.println(myConfig.getPort());
        System.out.println(myConfig.getUsername());
        return "==============";
    }

    //mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc
    // -Dversion=11.2.0.3 -Dpackaging=jar -Dfile=D:/BaiduNetdiskDownload
    // /ojdbc6-11.2.0.3.jar

    @GetMapping("/emp/emps")
    @ResponseBody
    public String getEmployeesCount(){
        return fbpEmployeesService.getEmployeesCount().toString();
    }

    @PutMapping("/emp/updateEmp")
    @ResponseBody
    public String updateEmployees(){
        fbpEmployeesService.updateEmployees();
        return "===";
    }

    @GetMapping("/soa/emp/getFbpEmployeesList")
    @ResponseBody
    public String getFbpEmployeesList(){
        Gson gson = new Gson();
        return gson.toJson(fbpEmployeesService.getFbpEmployeesList());
    }

}
