package com.example.demo.mapper;

import com.example.demo.model.ReceivedNotification;

/**
* @author haha
* @description 针对表【received_notification(RECEIVED_NOTIFICATION)】的数据库操作Mapper
* @createDate 2025-04-05 13:16:05
* @Entity com.example.demo.model.ReceivedNotification
*/
public interface ReceivedNotificationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ReceivedNotification record);

    int insertSelective(ReceivedNotification record);

    ReceivedNotification selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReceivedNotification record);

    int updateByPrimaryKey(ReceivedNotification record);

}
