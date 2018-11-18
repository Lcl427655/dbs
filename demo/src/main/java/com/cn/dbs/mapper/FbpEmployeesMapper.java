package com.cn.dbs.mapper;

import com.cn.dbs.entity.FbpEmployees;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

//@Mapper
//@MapperScan(value = {"com.cn.dbs.mapper"}),此处的包扫描，不能使用*，会报错
public interface FbpEmployeesMapper {
    
    @Select("SELECT * FROM FBP_EMPLOYEES WHERE id = #{id}")
    FbpEmployees getFbpEmployeesById(Integer id);

    @Select("SELECT s.* FROM FBP_EMPLOYEES S WHERE S.EMPLOYEE_ID < 30000 and S.EMPLOYEE_ID > 29990")
    List<FbpEmployees> getFbpEmployeesList();

    //@Insert("insert into FBP_EMPLOYEES(FbpEmployeesname, age, ctm) values(#{FbpEmployeesname}, #{age}, now())")
    //int add(FbpEmployees FbpEmployees);

    @Update("update fbp_employees s set s.created_by = 1" +
            " where s.enabled_flag = 'N'" +
            "   and s.employee_number = #{employeeNumber};")
    int update(@Param("employeeNumber") String employeeNumber, @Param("FbpEmployees") FbpEmployees FbpEmployees);

    //@Delete("DELETE from FBP_EMPLOYEES where id = #{id} ")
    //int delete(Integer id);
}
