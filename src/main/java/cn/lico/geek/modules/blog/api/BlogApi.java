package cn.lico.geek.modules.blog.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.blog.form.PageVo;
import cn.lico.geek.modules.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getBlogByLevel")
    public ResponseResult getBlogByLevel(@RequestParam(value = "currentPage",required = false) Integer currentPage,
                                         @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                         @RequestParam Integer level,
                                         @RequestParam Integer useSort){
        return blogService.getBlogByLevel(currentPage,pageSize,level,useSort);
    }

}
