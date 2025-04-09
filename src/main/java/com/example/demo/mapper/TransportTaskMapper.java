package com.example.demo.mapper;

import com.example.demo.model.TransportTask;
import org.apache.ibatis.annotations.Mapper;

/**
* @author haha
* @description 针对表【transport_task(TRANSPORT_TASK)】的数据库操作Mapper
* @createDate 2025-04-09 16:40:40
* @Entity com.example.demo.model.TransportTask
*/
@Mapper
public interface TransportTaskMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TransportTask record);

    int insertSelective(TransportTask record);

    TransportTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TransportTask record);

    int updateByPrimaryKey(TransportTask record);

}
