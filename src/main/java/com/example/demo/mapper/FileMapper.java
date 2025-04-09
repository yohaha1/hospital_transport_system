package com.example.demo.mapper;

import com.example.demo.model.File;
import org.apache.ibatis.annotations.Mapper;

/**
* @author haha
* @description 针对表【file(file)】的数据库操作Mapper
* @createDate 2025-04-09 16:40:40
* @Entity com.example.demo.model.File
*/
@Mapper
public interface FileMapper {

    int deleteByPrimaryKey(Long id);

    int insert(File record);

    int insertSelective(File record);

    File selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(File record);

    int updateByPrimaryKey(File record);

}
