package com.example.demo.mapper;

import com.example.demo.model.TaskNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author haha
* @description 针对表【task_node(TASK_NODE)】的数据库操作Mapper
* @createDate 2025-04-12 19:28:58
* @Entity com.example.demo.model.TaskNode
*/
@Mapper
public interface TaskNodeMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TaskNode record);

    int insertSelective(TaskNode record);

    TaskNode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskNode record);

    int updateByPrimaryKey(TaskNode record);

    TaskNode selectByTaskIdAndSequence(int taskId, int sequence);

    TaskNode selectByTaskIdAndDepartmentId(int taskId, int departmentId);

    List<TaskNode> selectByTaskId(int taskId);

    List<Integer> findNodesByDepartmentId(int departmentId);
}


