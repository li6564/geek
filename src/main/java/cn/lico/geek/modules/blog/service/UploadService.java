package cn.lico.geek.modules.blog.service;


import cn.lico.geek.core.api.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UploadService {
    Map uploadImg(MultipartFile img) throws IOException;

    ResponseResult uploadPics(HttpServletRequest request, List<MultipartFile> filedatas);
}
