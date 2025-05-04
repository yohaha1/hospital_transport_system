package com.example.demo.mapper;

import com.example.demo.model.ReceivedNotification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author haha
* @description 针对表【received_notification(RECEIVED_NOTIFICATION)】的数据库操作Mapper
* @createDate 2025-04-09 16:40:40
* @Entity com.example.demo.model.ReceivedNotification
*/
@Mapper
public interface ReceivedNotificationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ReceivedNotification record);

    int insertSelective(ReceivedNotification record);

    ReceivedNotification selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReceivedNotification record);

    int updateByPrimaryKey(ReceivedNotification record);

    List<Integer> getNotIdsByUserId(int userId);
}
