package com.example.demo.util;

import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.model.Department;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QRCodeValidator {

    private static final String SECRET_KEY = "jasiopdfuajdsnfo"; // 16 字节密钥
    private static final String ALGORITHM = "AES";

    private final DepartmentMapper departmentMapper;

    public QRCodeValidator(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    // 解密二维码内容
    public String decryptQRCode(String encryptedData) {
        System.out.println(encryptedData);
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new IllegalArgumentException("二维码解密失败", e);
        }
    }

    // 解析二维码内容为 Map
    public Map<String, String> parseQRCodeContent(String decryptedContent) {
        Map<String, String> qrData = new HashMap<>();
        decryptedContent = decryptedContent.replace("{", "").replace("}", ""); // 去除大括号
        String[] entries = decryptedContent.split(", ");
        for (String entry : entries) {
            String[] keyValue = entry.split("=");
            qrData.put(keyValue[0], keyValue[1]);
        }
        return qrData;
    }

    // 校验二维码是否过期
    public boolean isQRCodeExpired(String timestamp, long expirationTimeInSeconds) {
        long qrTimestamp = Long.parseLong(timestamp);
        long currentTime = Instant.now().getEpochSecond();
//        return (currentTime - qrTimestamp) > expirationTimeInSeconds;
        return false;
    }

    // 校验二维码内容
    public void validateQRCode(String encryptedQRCode, int expectedDepartmentId, long expirationTimeInSeconds) {
        // 解密二维码
        String decryptedContent = decryptQRCode(encryptedQRCode);
        Map<String, String> qrData = parseQRCodeContent(decryptedContent);

        // 验证二维码内容
        int departmentId = Integer.parseInt(qrData.get("departmentId"));
        if (departmentId != expectedDepartmentId) {
            Department nextDepartment = departmentMapper.selectByPrimaryKey((long) expectedDepartmentId);
            String nextDepartmentName = nextDepartment.getDepartmentname();
            throw new IllegalArgumentException("位置错误！应交接到: " + nextDepartmentName);
        }

        // 校验二维码是否过期
        String timestamp = qrData.get("timestamp");
        if (isQRCodeExpired(timestamp, expirationTimeInSeconds)) {
            throw new IllegalArgumentException("二维码已过期！");
        }
    }
}