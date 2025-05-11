package com.example.demo.service.impl;

import com.example.demo.mapper.TaskNodeMapper;
import com.example.demo.mapper.TransportTaskMapper;
import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private TaskNodeMapper taskNodeMapper;

    @Autowired
    private TransportTaskMapper transportTaskMapper;

    @Override
    public void handOverConfirm(int taskId) {
        // 1. 查询并找到下一个节点
        List<TaskNode> nodes = taskNodeMapper.selectByTaskId(taskId);
        int maxCompletedSeq = nodes.stream()
                .filter(n -> n.getHandovertime() != null)
                .mapToInt(TaskNode::getSequence)
                .max().orElse(0);
        TaskNode nextNode = nodes.stream()
                .filter(n -> n.getSequence() == maxCompletedSeq + 1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("无待处理节点"));

        // 2. 更新节点交接时间
        nextNode.setHandovertime(new Date());
        int updateCount = taskNodeMapper.updateByPrimaryKeySelective(nextNode);
        if (updateCount != 1) {
            throw new RuntimeException("更新任务节点交接时间失败！");
        }

        // 3. 如果是最后一个节点，则更新任务状态
        int maxSeqAll = nodes.stream().mapToInt(TaskNode::getSequence).max().orElse(0);
        if (nextNode.getSequence() == maxSeqAll) {
            TransportTask task = transportTaskMapper.selectByPrimaryKey((long) taskId);
            task.setStatus("DELIVERED");
            task.setCompletion(new Date());
            int taskUpdate = transportTaskMapper.updateByPrimaryKeySelective(task);
            if (taskUpdate != 1) {
                throw new RuntimeException("更新任务状态为 DELIVERED 失败！");
            }
        }
    }
}
