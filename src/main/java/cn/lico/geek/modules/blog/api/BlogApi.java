package cn.lico.geek.modules.blog.api;

import cn.lico.geek.core.annotation.BrowseTimes;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.blog.form.BlogPraiseCountForm;
import cn.lico.geek.modules.blog.form.PageVo;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author：linan
 * @Date：2022/11/8 11:37
 */
@RestController
@RequestMapping("/blog")
public class BlogApi {
    @Autowired
    private BlogService blogService;



    /**
     * 获取最新博客
     * @param pageVo
     * @return
     */
    @PostMapping("/getNewBlog")
    public ResponseResult getNewBlog(@RequestBody PageVo pageVo){
        return blogService.getNewBlog(pageVo);
    }

    /**
     * 根据推荐等级获取博客
     * @param currentPage
     * @param pageSize
     * @param level
     * @param useSort
     * @return
     */
    @GetMapping("/getBlogByLevel")
    public ResponseResult getBlogByLevel(@RequestParam(value = "currentPage",required = false) Integer currentPage,
                                         @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                         @RequestParam Integer level,
                                         @RequestParam Integer useSort){
        return blogService.getBlogByLevel(currentPage,pageSize,level,useSort);
    }

    /**
     * 根据博客id获取博文
     * @param oid
     * @param isLazy
     * @return
     */
    @GetMapping("/getBlogByUid")
    public ResponseResult getBlogByUid(@RequestParam Integer oid,
                                       @RequestParam Integer isLazy){
        //获取请求主机的IP地址
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String remoteHost = requestAttributes.getRequest().getRemoteHost();

        return blogService.getBlogByUid(oid,isLazy,remoteHost);
    }


}
