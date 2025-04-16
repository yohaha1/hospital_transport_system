package com.example.demo.response;

/**
 * 通用的 API 响应类
 * @param <T> 泛型，表示响应体的数据类型
 */
public class ApiResponse<T> {
    private boolean success; // 是否成功
    private T data;          // 返回的数据
    private String error;    // 错误信息

    // 构造函数
    public ApiResponse(boolean success, T data, String error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    // 工厂方法：用于快速生成成功响应
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // 工厂方法：用于快速生成失败响应
    public static <T> ApiResponse<T> failure(String error) {
        return new ApiResponse<>(false, null, error);
    }

    // Getter 和 Setter 方法
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}