package com.winter.common.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.winter.common.pojo.Response;
import com.winter.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
public class UploadController {
    @Value("${server.servlet.context-path}")
    private String apiPath;
    @Value("${app.upload-base-path}")
    private String uploadBasePath;
    @Value("${app.dvr-path}")
    private String dvrPath;
    private static final Set<String> whiteSet = new HashSet<>(Arrays.asList("jpg", "png", "bmp", "gif", "jpeg"));

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Response<String> uploadPrivate(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String type = FileTypeUtil.getType(this.bytesToHexString(file.getBytes()).substring(0, 20));
        if (!whiteSet.contains(type)) {
            throw new ServiceException("无效的文件");
        }
        String separator = File.separator;
        Date date = new Date();
        int year = DateUtil.year(date);
        int month = DateUtil.month(date) + 1;
        int day = DateUtil.dayOfMonth(date);
        StringBuilder folderStr = new StringBuilder(this.uploadBasePath);
        folderStr.append(separator).append("uploads")
                .append(separator).append(year)
                .append(separator).append(month)
                .append(separator).append(day);
        File folder = new File(folderStr.toString());
        if (!folder.exists()) {
            log.debug("文件夹不存在创建文件夹");
            folder.mkdirs();
        }
        String fileName = IdUtil.objectId() + "." + type;
        String filePath = folderStr.append(separator).append(fileName).toString();
        File newFile = new File(filePath);
        file.transferTo(newFile); // 保存文件
        String url = StrUtil.format("{}/uploads/{}/{}/{}/{}", this.apiPath, year, month, day, fileName);
        return Response.success(url);
    }

    /**
     * 请求图片资源
     *
     * @param request
     * @param response
     */
    @GetMapping("/uploads/**")
    public void getFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String t = this.uploadBasePath + uri.replace(this.apiPath, "");
        File file = new File(t);
        if (file.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                OutputStream out = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                assert in != null;
                in.close();
            }
        } else {
            throw new ServiceException("文件不存在");
        }
    }

    /**
     * 请求录播资源
     *
     * @param request
     * @param response
     */
    @GetMapping("/dvrs/**")
    public void getDvr(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String t = this.dvrPath + uri.replace(this.apiPath, "");
        File file = new File(t);
        if (file.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                OutputStream out = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                assert in != null;
                in.close();
            }
        } else {
            throw new ServiceException("文件不存在");
        }
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
