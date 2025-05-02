package com.example.demo.mapper;

import com.example.demo.model.FileInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author haha
* @description 针对表【file_info(file)】的数据库操作Mapper
* @createDate 2025-04-10 14:31:16
* @Entity com.example.demo.model.FileInfo
*/
@Mapper
public interface FileInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(FileInfo record);

    int insertSelective(FileInfo record);

    FileInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FileInfo record);

    int updateByPrimaryKey(FileInfo record);

    List<FileInfo> selectByTaskId(int taskId);
}

