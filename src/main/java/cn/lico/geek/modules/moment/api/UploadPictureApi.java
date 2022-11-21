package cn.lico.geek.modules.moment.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author：linan
 * @Date：2022/11/21 16:54
 */
@RestController
@RequestMapping("/momentfile")
public class UploadPictureApi {

    @Autowired
    private UploadService uploadService;

    /**
     *动态模块上传图片
     * @param request
     * @param filedatas
     * @return
     */
    @PostMapping("/pictures")
    public ResponseResult uploadPics(HttpServletRequest request, List<MultipartFile> filedatas){
        return uploadService.uploadPics(request,filedatas);
    }
}
