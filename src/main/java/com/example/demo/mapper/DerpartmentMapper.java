package com.example.demo.mapper;

import com.example.demo.model.Derpartment;

/**
* @author haha
* @description 针对表【derpartment(DERPARTMENT)】的数据库操作Mapper
* @createDate 2025-04-05 13:15:06
* @Entity com.example.demo.model.Derpartment
*/
public interface DerpartmentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Derpartment record);

    int insertSelective(Derpartment record);

    Derpartment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Derpartment record);

    int updateByPrimaryKey(Derpartment record);

}
