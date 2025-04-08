package com.example.demo.mapper;

import com.example.demo.model.LocationRecord;
import org.apache.ibatis.annotations.Mapper;

/**
* @author haha
* @description 针对表【location_record(LOCATION_RECORD)】的数据库操作Mapper
* @createDate 2025-04-05 13:15:55
* @Entity com.example.demo.model.LocationRecord
*/
@Mapper
public interface LocationRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(LocationRecord record);

    int insertSelective(LocationRecord record);

    LocationRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LocationRecord record);

    int updateByPrimaryKey(LocationRecord record);

}
