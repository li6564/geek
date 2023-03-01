package cn.lico.geek.modules.blog.service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.core.exception.SystemException;
import cn.lico.geek.modules.blog.service.UploadService;
import cn.lico.geek.modules.moment.dto.UploadDto;
import cn.lico.geek.utils.PathUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class OssUploadService implements UploadService {
    /**
     * 上传图片
     * @param img
     * @return
     */
    @Override
    public Map uploadImg(MultipartFile img) {
        //判断文件类型
        //获取原始文件名
        String originalFilename = img.getOriginalFilename()+".png";
        //对原始文件名进行判断

        if(!originalFilename.endsWith(".png")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //如果判断通过上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);

        return uploadOss(img,filePath);
    }

    /**
     * 动态模块上传多个图片
     * @param request
     * @param filedatas
     * @return
     */
    @Override
    public ResponseResult uploadPics(HttpServletRequest request, List<MultipartFile> filedatas) {
        List<UploadDto> list = new ArrayList<>();
        //传来的遍历图片集合
        if (filedatas.size()>0){
            for (MultipartFile filedata : filedatas) {
                Map map = uploadImg(filedata);
                list.add(new UploadDto(map.get("url").toString()));
            }
        }
        return new ResponseResult(list);
    }

    private String accessKey;
    private String secretKey;
    private String bucket;


    private Map uploadOss(MultipartFile imgFile, String filePath){
        Map<String,Object> map = new HashMap<>();
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                map.put("url","http://qn.yunwaishequ.cn/"+key);
                map.put("uploaded",1);
                int i = key.lastIndexOf("/");
                int m = key.lastIndexOf(".");
                map.put("fileName",key.substring(i+1));
                map.put("uid",key.substring(i+1,m));
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return map;
    }
}

