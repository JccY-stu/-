package com.southwind.mmall002.service.impl.file;

import com.southwind.mmall002.controller.file.UploadController;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.service.file.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * @Author Chengzhi
 * @Date 2021/5/15 11:27
 * @Version 1.0
 * @param file 需要上传的文件
 * @param session 获得正在执行操作的该用户
 */
@Service
public class UploadServiceImpl implements UploadService {

    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    /**
     * 上传 用户头像
     * @param file
     * @param session
     * @return
     */
    @Override
    public String uploadHead(@RequestParam("file") MultipartFile file, HttpSession session) {
        if (file.isEmpty()) {
            //抛出异常
        }

        //生成用户专属的文件名
        User user = (User) session.getAttribute("user");
        //文件名为用户名 + 文件名 此处有bug 如果用户的文件名太长则存不下，可以改用生成随机名
        //或者在前端加一个验证，判断文件名是否过长
        String fileName = user.getId()+file.getOriginalFilename();

        //此处必须填写绝对路径！！！
        String filePath = "C:\\Users\\橙汁\\Desktop\\mmall-5.17\\src\\main\\resources\\static\\images\\";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            LOGGER.info("上传成功");
            return fileName;
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return "上传失败！";
    }

    /**
     * 上传 商品图片到项目地址
     * @param file
     * @param session
     * @return
     */
    @Override
    public String uploadProductPic(MultipartFile file,HttpSession session) {
        if (file.isEmpty()) {
            //抛出异常
        }
        //生成用户专属的文件名
        User user = (User) session.getAttribute("user");
        //文件名为用户名 + 文件名 此处有bug 如果用户的文件名太长则存不下，可以改用生成随机名
        //或者在前端加一个验证，判断文件名是否过长
        String fileName = user.getId()+file.getOriginalFilename();

        //此处必须填写绝对路径！！！
        String filePath = "C:\\Users\\橙汁\\Desktop\\mmall-5.17\\src\\main\\resources\\static\\images\\";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            LOGGER.info("上传成功");
            return fileName;
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return "上传失败！";
    }
}
