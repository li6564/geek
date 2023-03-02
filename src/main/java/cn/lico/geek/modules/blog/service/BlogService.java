package cn.lico.geek.modules.blog.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.form.BlogForm;
import cn.lico.geek.modules.blog.form.PageVo;
import cn.lico.geek.modules.user.form.UserBlogForm;
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

    ResponseResult getBlogListByUser(UserBlogForm userBlogForm);

    ResponseResult searchBlogByTag(String tagUid, Integer currentPage, Integer pageSize);

    ResponseResult searchBlogBySort(String blogSortUid, Integer currentPage, Integer pageSize);

    ResponseResult getMeBlogList(Integer currentPage, Integer pageSize);

    ResponseResult publish(String isPublish, String uid);

    ResponseResult deleteByUid(String uid);
}
