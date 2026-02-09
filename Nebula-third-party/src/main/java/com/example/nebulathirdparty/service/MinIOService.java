package com.example.nebulathirdparty.service;

import io.minio.MinioClient;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MinIOService {
    
    @Autowired
    private MinioClient minioClient;
    
    @Value("${minio.endpoint}")
    private String endpoint;
    
    @Value("${minio.bucket-name}")
    private String bucket;
    
    /**
     * 获取前端直传的签名信息
     */
    public Map<String, Object> getPresignedPostPolicy(String objectName) throws Exception {
        Map<String, String> formData = new HashMap<>();
        formData.put("bucket", bucket);
        formData.put("key", objectName);
        formData.put("Content-Type", "application/octet-stream");
        
        Map<String, Object> result = new HashMap<>();
        result.put("endpoint", endpoint);
        result.put("bucket", bucket);
        result.put("objectName", objectName);
        result.put("formData", formData);
        
        return result;
    }
    
    /**
     * 获取预签名 URL（用于上传或下载）
     */
    public String getPresignedObjectUrl(String objectName, String method) throws Exception {
        // MinIO 7.1.0 API
        io.minio.http.Method httpMethod = "PUT".equalsIgnoreCase(method) 
            ? io.minio.http.Method.PUT 
            : io.minio.http.Method.GET;
        
        return minioClient.getPresignedObjectUrl(
            httpMethod,
            bucket,
            objectName,
            604800,  // 7天有效期（604800秒）
            null
        );
    }

    /**
     * 获取预签名 URL（仅用于下载，兼容旧接口）
     */
    public String getPresignedObjectUrl(String objectName) throws Exception {
        return getPresignedObjectUrl(objectName, "GET");
    }

    /**
     * 获取直接访问的 URL（不过期）
     * 用于图片等资源的直接访问，bucket需要配置为public
     */
    public String getDirectAccessUrl(String objectName) throws Exception {
        // 直接构造公开访问URL：http://endpoint/bucket/objectName
        String normalizedEndpoint = endpoint;
        if (normalizedEndpoint.endsWith("/")) {
            normalizedEndpoint = normalizedEndpoint.substring(0, normalizedEndpoint.length() - 1);
        }
        
        if (objectName.startsWith("/")) {
            objectName = objectName.substring(1);
        }
        
        return normalizedEndpoint + "/" + bucket + "/" + objectName;
    }
}
