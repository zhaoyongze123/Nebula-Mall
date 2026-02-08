package com.example.nebulathirdparty.controller;

import com.common.utils.R;
import com.example.nebulathirdparty.service.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/thirdparty/minio")
@CrossOrigin
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
            // 生成唯一的对象名称
            String objectName = UUID.randomUUID().toString() + "/" + fileName;
            Map<String, Object> policy = minIOService.getPresignedPostPolicy(objectName);
            return R.ok(policy);
        } catch (Exception e) {
            return R.error("获取签名失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取下载 URL
     */
    @GetMapping("/download/url")
    public R getDownloadUrl(@RequestParam String objectName) {
        try {
            String url = minIOService.getPresignedObjectUrl(objectName);
            return R.ok().put("url", url);
        } catch (Exception e) {
            return R.error("获取下载地址失败: " + e.getMessage());
        }
    }
}
