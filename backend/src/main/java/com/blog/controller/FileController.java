package com.blog.controller;

import com.blog.common.ResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${upload.path:d:/code/AiCode/PersonalBlogSystem/file}")
    private String uploadPath;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        if (file == null || file.isEmpty()) {
            result.put("code", ResponseCode.PARAM_ERROR.getCode());
            result.put("message", "文件不能为空");
            return result;
        }

        // 允许的文件类型
        List<String> allowedExtensions = Arrays.asList("md", "txt", "markdown");
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";

        if (!allowedExtensions.contains(extension)) {
            result.put("code", ResponseCode.PARAM_ERROR.getCode());
            result.put("message", "只允许上传 .md, .txt 文件");
            return result;
        }

        // 文件大小限制 10MB
        if (file.getSize() > 10 * 1024 * 1024) {
            result.put("code", ResponseCode.PARAM_ERROR.getCode());
            result.put("message", "文件大小不能超过 10MB");
            return result;
        }

        try {
            // 按日期分目录
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            File uploadDir = new File(uploadPath, datePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一文件名
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String newFilename = uuid + "." + extension;
            File destFile = new File(uploadDir, newFilename);

            // 保存文件
            file.transferTo(destFile);

            // 返回相对路径（用于存入数据库）
            String relativePath = datePath + "/" + newFilename;
            String fullUrl = contextPath + "/file/" + relativePath;

            result.put("code", ResponseCode.SUCCESS.getCode());
            result.put("message", "上传成功");
            Map<String, Object> data = new HashMap<>();
            data.put("path", relativePath);
            data.put("url", fullUrl);
            data.put("originalName", originalFilename);
            result.put("data", data);

        } catch (IOException e) {
            result.put("code", ResponseCode.INTERNAL_ERROR.getCode());
            result.put("message", "文件保存失败: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/upload/image")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        if (file == null || file.isEmpty()) {
            result.put("code", ResponseCode.PARAM_ERROR.getCode());
            result.put("message", "图片不能为空");
            return result;
        }

        // 允许的图片类型
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";

        if (!allowedExtensions.contains(extension)) {
            result.put("code", ResponseCode.PARAM_ERROR.getCode());
            result.put("message", "只允许上传 jpg, png, gif, webp 图片");
            return result;
        }

        // 图片大小限制 5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            result.put("code", ResponseCode.PARAM_ERROR.getCode());
            result.put("message", "图片大小不能超过 5MB");
            return result;
        }

        try {
            // 图片存放到 images 目录
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            File uploadDir = new File(uploadPath, "images/" + datePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一文件名
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String newFilename = uuid + "." + extension;
            File destFile = new File(uploadDir, newFilename);

            // 保存文件
            file.transferTo(destFile);

            // 返回 URL，拼接 context-path
            String baseUrl = contextPath + "/file";
            String fullUrl = baseUrl + "/images/" + datePath + "/" + newFilename;

            result.put("code", ResponseCode.SUCCESS.getCode());
            result.put("message", "上传成功");
            Map<String, Object> data = new HashMap<>();
            data.put("url", fullUrl);
            data.put("originalName", originalFilename);
            result.put("data", data);

        } catch (IOException e) {
            result.put("code", ResponseCode.INTERNAL_ERROR.getCode());
            result.put("message", "图片保存失败: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/save-markdown")
    public Map<String, Object> saveMarkdown(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();

        String content = (String) body.get("content");
        String filename = (String) body.get("filename");

        if (content == null || content.isEmpty()) {
            result.put("code", ResponseCode.PARAM_ERROR.getCode());
            result.put("message", "内容不能为空");
            return result;
        }

        try {
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            File uploadDir = new File(uploadPath, "markdown/" + datePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uuid = UUID.randomUUID().toString().replace("-", "");
            String finalFilename = (filename != null && !filename.isEmpty())
                ? (filename.endsWith(".md") ? filename : filename + ".md")
                : (uuid + ".md");

            File destFile = new File(uploadDir, finalFilename);

            try (FileWriter writer = new FileWriter(destFile)) {
                writer.write(content);
            }

            String relativePath = "markdown/" + datePath + "/" + finalFilename;
            String fullUrl = contextPath + "/file/" + relativePath;

            result.put("code", ResponseCode.SUCCESS.getCode());
            result.put("message", "保存成功");
            Map<String, Object> data = new HashMap<>();
            data.put("path", relativePath);
            data.put("url", fullUrl);
            result.put("data", data);

        } catch (IOException e) {
            result.put("code", ResponseCode.INTERNAL_ERROR.getCode());
            result.put("message", "保存失败: " + e.getMessage());
        }

        return result;
    }
}
