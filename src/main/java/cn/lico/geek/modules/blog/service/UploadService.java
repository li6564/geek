package cn.lico.geek.modules.blog.service;


import cn.lico.geek.core.api.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UploadService {
    Map uploadImg(MultipartFile img) throws IOException;
}
