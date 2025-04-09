package com.example.demo.mapper;

import com.example.demo.model.Derpartment;
import org.apache.ibatis.annotations.Mapper;

/**
* @author haha
* @description 针对表【derpartment(DERPARTMENT)】的数据库操作Mapper
* @createDate 2025-04-09 16:40:40
* @Entity com.example.demo.model.Derpartment
*/
@Mapper
public interface DerpartmentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Derpartment record);

    int insertSelective(Derpartment record);

    Derpartment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Derpartment record);

    int updateByPrimaryKey(Derpartment record);

}
