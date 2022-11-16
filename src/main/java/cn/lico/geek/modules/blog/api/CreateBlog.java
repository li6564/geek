package cn.lico.geek.modules.blog.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.form.BlogForm;
import cn.lico.geek.modules.blog.form.PageVo;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.modules.blog.service.BlogSortService;
import cn.lico.geek.modules.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：linan
 * @Date：2022/11/15 14:35
 */
@RestController()
@RequestMapping("createBlog")
public class CreateBlog {
    @Autowired
    private TagService tagService;

    @Autowired
    private BlogSortService blogSortService;

    @Autowired
    private BlogService blogService;

    //获取全部博客分类
    @PostMapping("/getBlogSortList")
    public ResponseResult getBlogSortList(@RequestBody PageVo pageVo){
        return blogSortService.getBlogSortList(pageVo);
    }
    //获取全部博客标签
    @PostMapping("/getBlogTagList")
    public ResponseResult getBlogTagList(@RequestBody PageVo pageVo){
        return tagService.getBlogTagList(pageVo);

    }

    //发布博客
    @PostMapping("/add")
    public ResponseResult addBlog(@RequestBody BlogForm blogForm){
        return blogService.addBlog(blogForm);
    }

}
