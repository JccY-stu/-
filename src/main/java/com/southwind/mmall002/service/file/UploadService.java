package com.southwind.mmall002.service.file;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * @Author Chengzhi
 * @Date 2021/5/15 11:26
 * @Version 1.0
 */
public interface UploadService {

    public String uploadHead(@RequestParam("file") MultipartFile file, HttpSession session);

    public String uploadProductPic(@RequestParam("file") MultipartFile file,HttpSession session);
}
