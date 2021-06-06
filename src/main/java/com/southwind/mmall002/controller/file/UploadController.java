package com.southwind.mmall002.controller.file;

import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.service.UserService;
import com.southwind.mmall002.service.file.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description
 * @Author sgl
 * @Date 2018-05-15 14:04
 */
@Controller()
@RequestMapping("/file")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @Autowired
    UserService userService;


    @ResponseBody
    @PostMapping("/upLoadHead")
    public Map<String,Object> uploadHead(@RequestParam("file") MultipartFile file, HttpSession session) {
        //调用 UploadService 上传到 /static/images 目录下
        //获得新头像的文件名
        Map map = new HashMap<String,Object>();
        String fileName = "";
        try {
            fileName = uploadService.uploadHead(file, session);
        }catch (Exception e) {
            map.put("msg","error");
            map.put("code",0);
            e.printStackTrace();
        }
        //更新数据库 user 表中的 filename
        User user = (User) session.getAttribute("user");
        user.setFileName(fileName);
        //通过 id 更新
        userService.updateById(user);
        //返回json
        map.put("msg","ok");
        map.put("code",200);
            /*加不加这个都行
            map.put("data",new HashMap<String,Object>(){
                {
                    put("src",savepath);
                }
            });*/
        return map;

    }
}

