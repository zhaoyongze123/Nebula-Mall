package com.example.nebulathirdparty.controller;

import com.common.utils.R;
import com.example.nebulathirdparty.service.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/thirdparty/minio")
public class MinIOController {
    
    @Autowired
    private MinIOService minIOService;
    
    /**
     * 获取上传签名
     */
    @GetMapping("/upload/signature")
    public R getUploadSignature(@RequestParam(value = "fileName", required = false) String fileName) {
        try {
            // 如果未提供文件名，则自动生成
            if (fileName == null || fileName.trim().isEmpty()) {
                fileName = "file-" + System.currentTimeMillis();
            }
            
            // 按日期创建目录：yyyy-MM-dd/uuid/filename
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String objectName = date + "/" + UUID.randomUUID().toString() + "/" + fileName;
            
            Map<String, Object> policy = minIOService.getPresignedPostPolicy(objectName);
            // 标准格式：{ code: 0, msg: "success", data: { ... } }
            return R.ok().put("data", policy);
        } catch (Exception e) {
            return R.error("获取签名失败: " + e.getMessage());
        }
    }

    /**
     * 获取上传URL（presigned PUT URL）
     */
    @GetMapping("/upload/url")
    public R getUploadUrl(@RequestParam String objectName) {
        try {
            String url = minIOService.getPresignedObjectUrl(objectName, "PUT");
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("url", url);
            return R.ok().put("data", result);
        } catch (Exception e) {
            return R.error("获取上传URL失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取下载 URL
     */
    @GetMapping("/download/url")
    public R getDownloadUrl(@RequestParam String objectName) {
        try {
            String url = minIOService.getPresignedObjectUrl(objectName, "GET");
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("url", url);
            return R.ok().put("data", result);
        } catch (Exception e) {
            return R.error("获取下载地址失败: " + e.getMessage());
        }
    }
}
