package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author haha
* @description 针对表【user(USER)】的数据库操作Mapper
* @createDate 2025-04-09 16:40:20
* @Entity com.example.demo.model.User
*/
@Mapper
public interface UserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    // 新增通过用户名查询用户的方法
    User selectByUsername(String username);

    List<Integer> findDoctorIdsByDepartmentId(int departmentId);

    int getDepartmentIdByUserId(int userId);

    int getAllTransCount();

    List<User> getAllUsers();
}

