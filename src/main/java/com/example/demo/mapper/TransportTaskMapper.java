package com.example.demo.mapper;

import com.example.demo.model.TransportTask;

/**
* @author haha
* @description 针对表【transport_task(TRANSPORT_TASK)】的数据库操作Mapper
* @createDate 2025-04-05 13:16:10
* @Entity com.example.demo.model.TransportTask
*/
public interface TransportTaskMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TransportTask record);

    int insertSelective(TransportTask record);

    TransportTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TransportTask record);

    int updateByPrimaryKey(TransportTask record);

}
