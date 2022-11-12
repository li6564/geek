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
@RequestMapping("/index")
public class BlogApi {
    @Autowired
    private BlogService blogService;

    @PostMapping("/getNewBlog")
    public ResponseResult getBlogByLevel(@RequestBody PageVo pageVo){
        return blogService.getNewBlog(pageVo);
    }
}
