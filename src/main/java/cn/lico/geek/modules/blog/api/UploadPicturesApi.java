package cn.lico.geek.modules.blog.api;

import cn.lico.geek.modules.blog.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author：linan
 * @Date：2022/11/15 16:18
 */
@RestController
@RequestMapping("/mogu-picture/file")
public class UploadPicturesApi {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/ckeditorUploadFile")
    public Map uploadPicture(HttpServletRequest request){
        // 转换成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
// 取得request中的所有文件名
        Iterator<String> iter = multiRequest.getFileNames();
        List<MultipartFile> multipartFileList = new ArrayList<>();
        MultipartFile multipartFile = null;
        while (iter.hasNext()) {
            // 只取一个
            multipartFile = multiRequest.getFile(iter.next());
            break;
        }
        try {
            return uploadService.uploadImg(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }
    }
}
