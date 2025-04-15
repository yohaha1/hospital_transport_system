package com.example.demo.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    private static final String SECRET_KEY = "jasiopdfuajdsnfo";
    private static final String ALGORITHM = "AES";


    public static void generateQRCode(String content, String filePath) throws WriterException, IOException {
        System.out.println("rqqqqqqqqqqqqqqqqqqqqqqqqqqqqq  "+content);
        int width = 300;
        int height = 300;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        File imageFile = new File(filePath);
        File parentDir = imageFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs(); // 创建父目录
        }

        ImageIO.write(image, "png", imageFile);
    }

    public static String generateQRCodeContent(int departmentId) {
        long timestamp = Instant.now().getEpochSecond();
        Map<String, String> qrData = new HashMap<>();
        qrData.put("departmentId", String.valueOf(departmentId));
        qrData.put("timestamp", String.valueOf(timestamp));
        String content = qrData.toString();

        // 加密
        return encrypt(content, SECRET_KEY);
    }

    //AES加密
    private static String encrypt(String data, String key) {
        try {
            // 创建 AES 密钥
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);

            // 初始化 Cipher 为加密模式
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // 加密数据
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());

            // 将加密后的字节数组转换为 Base64 编码字符串
            return Base64.getUrlEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    public static void main(String[] args) throws WriterException, IOException {
        int departmentId = 101;

        String content = generateQRCodeContent(departmentId);

        String filePath = "D:/project/graduate_project/data/qrCode/department_" + departmentId + ".png";
        generateQRCode(content, filePath);

        System.out.println("QR Code generated and saved to: " + filePath);
    }
}