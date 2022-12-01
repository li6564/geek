package cn.lico.geek.modules.news.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.news.exception.NewsServiceException;
import cn.lico.geek.modules.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author：linan
 * @Date：2022/11/30 20:36
 */
@RestController
@RequestMapping("/news")
public class NewsApi {

    @Autowired
    private NewsService newsService;

    /**
     * 获取资讯列表
     * @return
     */
    @GetMapping("/getlist")
    public ResponseResult getlist(){
        return newsService.getlist();
    }

    @GetMapping("/getNewsByUid")
    public ResponseResult getNewsByUid(@RequestParam Integer oid,@RequestParam Integer isLazy){
        //获取请求主机的IP地址
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String remoteHost = requestAttributes.getRequest().getRemoteHost();
        try {
            return newsService.getNewsByUid(remoteHost,oid,isLazy);
        } catch (NewsServiceException e) {
            e.printStackTrace();
            return new ResponseResult("当前资讯不存在","error");
        }
    }
}
