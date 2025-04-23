package com.example.demo.mapper;

import com.example.demo.model.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author haha
* @description 针对表【department(DERPARTMENT)】的数据库操作Mapper
* @createDate 2025-04-13 17:19:32
* @Entity com.example.demo.model.Department
*/
@Mapper
public interface DepartmentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

    List<Integer> getAllDepartmentIds();

    List<Department> getAllDepartments();
}
