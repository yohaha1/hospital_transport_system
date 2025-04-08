package com.example.demo.mapper;

import com.example.demo.model.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
* @author haha
* @description 针对表【notification(NOTIFICATION)】的数据库操作Mapper
* @createDate 2025-04-05 13:16:01
* @Entity com.example.demo.model.Notification
*/
@Mapper
public interface NotificationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Notification record);

    int insertSelective(Notification record);

    Notification selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Notification record);

    int updateByPrimaryKey(Notification record);

}
