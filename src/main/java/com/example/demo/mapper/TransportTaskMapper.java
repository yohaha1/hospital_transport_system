package com.example.demo.mapper;

import com.example.demo.model.TransportTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
* @author haha
* @description 针对表【transport_task(TRANSPORT_TASK)】的数据库操作Mapper
* @createDate 2025-04-21 19:30:12
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

    List<TransportTask> findByFilters(String status, Date startDate, Date endDate);

    List<TransportTask> findByTransporterAndFilters(int transporterId,String status, Date startDate, Date endDate);

    List<TransportTask> findByDepartmentDoctorIdsAndFilters(List<Integer> doctorIds, String status, Date startDate, Date endDate);

    List<TransportTask> getStatusTasks(String status);

    List<String> getAllTypes();

    int getBusyTransCount();
}
