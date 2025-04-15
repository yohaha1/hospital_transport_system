package com.example.demo.util;

import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.util.QRCodeGenerator;
import com.google.zxing.WriterException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class QRCodeScheduler {

    private final DepartmentMapper departmentMapper;

    public QRCodeScheduler(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Scheduled(fixedRate = 120000) // 每两分钟运行一次
    public void updateQRCode() {
        try {
            List<Integer> departmentIds = departmentMapper.getAllDepartmentIds();
            for (int departmentId : departmentIds) {
                try {
                    // 生成二维码内容
                    String content = QRCodeGenerator.generateQRCodeContent(departmentId);

                    // 保存二维码图片
                    String filePath = "D:/project/graduate_project/data/qrCode/department_" + departmentId + ".png";
                    QRCodeGenerator.generateQRCode(content, filePath);

                    System.out.println("已生成部门 " + departmentId + " 的二维码: " + content);
                } catch (IOException e) {
                    System.err.println("IO 错误：无法生成部门 " + departmentId + " 的二维码。原因: " + e.getMessage());
                } catch (WriterException e) {
                    System.err.println("二维码生成错误：无法生成部门 " + departmentId + " 的二维码。原因: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("二维码更新任务失败：" + e.getMessage());
        }
    }
}