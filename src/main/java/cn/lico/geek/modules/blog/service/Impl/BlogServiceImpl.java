package cn.lico.geek.modules.blog.service.Impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.entity.BlogSort;
import cn.lico.geek.modules.blog.entity.BlogTag;
import cn.lico.geek.modules.blog.form.PageVo;
import cn.lico.geek.modules.blog.mapper.BlogMapper;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.modules.blog.service.BlogSortService;
import cn.lico.geek.modules.blog.service.BlogTagService;
import cn.lico.geek.modules.blog.vo.NewBlogUserVo;
import cn.lico.geek.modules.blog.vo.NewBlogVo;
import cn.lico.geek.modules.tag.entity.Tag;
import cn.lico.geek.modules.tag.service.TagService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDto;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author：linan
 * @Date：2022/11/8 11:56
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogSortService blogSortService;

    @Autowired
    private BlogTagService blogTagService;

    /**
     * 根据文章等级进行推荐展示
     * @param currentPage
     * @param pageSize
     * @param level
     * @param useSort
     * @return
     */
    @Override
    public ResponseResult getBlogByLevel(Integer currentPage, Integer pageSize, Integer level, Integer useSort) {
        return null;
    }

    /**
     * 根据分类id获取文章列表
     * @param pageVo
     * @return
     */
    @Override
    public ResponseResult getNewBlog(PageVo pageVo) {
        //根据条件查询
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        //判断blogSortUid是否为空，如果不为空则加入判断条件
        queryWrapper.eq(Objects.nonNull(pageVo.getBlogSortUid())&&pageVo.getBlogSortUid().length()>0,Blog::getBlogSortUid,pageVo.getBlogSortUid());
        //进行分页查询
        if ("create_time".equals(pageVo.getOrderByDescColumn())){
            queryWrapper.orderByDesc(Blog::getCreateTime);
        }else if ("click_count".equals(pageVo.getOrderByDescColumn())){
            queryWrapper.orderByDesc(Blog::getClickCount);
        }else {
            queryWrapper.orderByDesc(Blog::getCreateTime);
        }
        Page<Blog> page  = new Page<>(pageVo.getCurrentPage(),pageVo.getPageSize());
        page(page, queryWrapper);

        //将pages中的信息转换到NewBlogVo中
        List<NewBlogVo> newBlogVos = BeanCopyUtils.copyBeanList(page.getRecords(), NewBlogVo.class);

        //填充uer信息
        getUserInformation(newBlogVos);

        //填充 标签信息
        getTagInformation(newBlogVos);

        //填充分类信息
        getSortInformation(newBlogVos);

        PageDTO<NewBlogVo> pageDto = new PageDTO<>();
        pageDto.setRecords(newBlogVos);
        pageDto.setCurrent((int)page.getCurrent());
        pageDto.setTotal((int)page.getTotal());
        pageDto.setSize((int)page.getSize());

        return new ResponseResult(pageDto);
    }

    /**
     * 填充分类信息
     * @param newBlogVos
     */
    private void getSortInformation(List<NewBlogVo> newBlogVos) {
        for (NewBlogVo newBlogVo : newBlogVos) {
            Blog blog = getById(newBlogVo.getUid());
            String blogSortUid = blog.getBlogSortUid();

            BlogSort blogSort = blogSortService.getById(blogSortUid);

            newBlogVo.setBlogSort(blogSort);
        }
    }

    /**
     * 填充 标签信息
     * @param newBlogVos
     */
    private void getTagInformation(List<NewBlogVo> newBlogVos) {

        for (NewBlogVo newBlogVo : newBlogVos) {
            //根据blogUid获取BlogTag列表
            LambdaQueryWrapper<BlogTag> queryWrapper  = new LambdaQueryWrapper<>();
            queryWrapper.eq(BlogTag::getBlogId,newBlogVo.getUid());
            //获取blogTag列表
            List<BlogTag> blogTags = blogTagService.list(queryWrapper);
            //获取tag id列表
            List<String> TagIds = new ArrayList<>();
            for (BlogTag blogTag : blogTags) {
                TagIds.add(blogTag.getTagId());
            }
            List<Tag> tags = new ArrayList<>();
            //根据tagid列表 获取标签
            if (TagIds.size()!=0){
                tags = tagService.listByIds(TagIds);
            }
            newBlogVo.setTagList(tags);
        }
    }

    /**
     * 填充uer信息
     * @param newBlogVos
     */
    public void getUserInformation(List<NewBlogVo> newBlogVos){

        //遍历newBlogVos为每一个newBlogVos填充user信息
        for (NewBlogVo newBlogVo : newBlogVos) {
            //获取博客
            Blog blog = getById(newBlogVo.getUid());
            //获取userid
            String userUid = blog.getUserUid();
            //获取user
            User user = userService.getById(userUid);
            //将uer转化为需要的形式
            NewBlogUserVo newBlogUserVo = null;
            if (Objects.nonNull(user)){
                newBlogUserVo = BeanCopyUtils.copyBean(user, NewBlogUserVo.class);
            }

            //填充user信息
            newBlogVo.setUser(newBlogUserVo);
        }
    }
}
