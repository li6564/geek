package cn.lico.geek.modules.blog.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.form.BlogForm;
import cn.lico.geek.modules.blog.form.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/8 11:56
 */
public interface BlogService extends IService<Blog> {


    ResponseResult getBlogByLevel(Integer currentPage, Integer pageSize, Integer level, Integer useSort);

    ResponseResult getNewBlog(PageVo pageVo);

    ResponseResult addBlog(BlogForm blogForm);

    ResponseResult getBlogByUid(Integer oid, Integer isLazy,String remeteHost);
}
