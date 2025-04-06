package com.example.demo.mapper;

import com.example.demo.model.TaskNode;

/**
* @author haha
* @description 针对表【task_node(TASK_NODE)】的数据库操作Mapper
* @createDate 2025-04-05 13:16:08
* @Entity com.example.demo.model.TaskNode
*/
public interface TaskNodeMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TaskNode record);

    int insertSelective(TaskNode record);

    TaskNode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskNode record);

    int updateByPrimaryKey(TaskNode record);

}
