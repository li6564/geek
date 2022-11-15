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
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDto;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
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

    @Autowired
    private UserWatchService userWatchService;

    @Autowired
    private UserPraiseRecordService userPraiseRecordService;

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
        //根据level条件进行判断
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getLevel,level);
        //进行二三级推荐
        PageDTO<NewBlogVo> pageDTO = new PageDTO<>();
        if (Objects.nonNull(currentPage)&&Objects.nonNull(pageSize)){
            //用分页实现选取 pageSize 条数据
            Page<Blog> page = new Page<>(currentPage,pageSize);
            page(page,queryWrapper);
            //封装到分页对象中
            List<NewBlogVo> newBlogVos = BeanCopyUtils.copyBeanList(page.getRecords(), NewBlogVo.class);
            pageDTO.setRecords(newBlogVos);
            pageDTO.setCurrent((int)page.getCurrent());
            pageDTO.setTotal((int)page.getTotal());
            pageDTO.setSize((int)page.getSize());
        }else {//进行一级推荐
            //获取所有的一级推荐
            List<Blog> list = list(queryWrapper);
            List<NewBlogVo> newBlogVos = BeanCopyUtils.copyBeanList(list, NewBlogVo.class);
            pageDTO.setRecords(newBlogVos);
            pageDTO.setSize(list.size());
            pageDTO.setTotal(list.size());
            pageDTO.setCurrent(1);
        }
        return new ResponseResult(pageDTO);

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
        if (Objects.nonNull(pageVo.getOrderBy())&&pageVo.getOrderBy().length()>0){
            //获取用戶id
            String userId = SecurityUtils.getUserId();
            LambdaQueryWrapper<UserWatch> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(UserWatch::getUserUid,userId);
            List<UserWatch> list = userWatchService.list(queryWrapper1);
            List Ids = new ArrayList();
            for (UserWatch userWatch : list) {
                Ids.add(userWatch.getToUserUid());
            }
            if (Objects.isNull(Ids)||Ids.size()== 0){
                queryWrapper.eq(Blog::getUserUid,"-1");
            }else {
                queryWrapper.in(Blog::getUserUid,Ids);
            }

        }
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


        //填充点赞数量
        getPraiseCount(newBlogVos);

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
     * 填充点赞数量(有bug)
     * @param newBlogVos
     */
    private void getPraiseCount(List<NewBlogVo> newBlogVos) {
        LambdaQueryWrapper<UserPraiseRecord> queryWrapper = new LambdaQueryWrapper<>();
        for (NewBlogVo newBlogVo : newBlogVos) {
            //aa6e71e0c243b921ca657a900b4b442f
            String uid = newBlogVo.getUid();
            //queryWrapper.eq(UserPraiseRecord::getResourceUid,newBlogVo.getUid());
            queryWrapper.eq(UserPraiseRecord::getResourceUid,uid)
                    .eq(UserPraiseRecord::getPraiseType,1)
                    .eq(UserPraiseRecord::getStatus,1);
            int praiseCount = userPraiseRecordService.count(queryWrapper);
            //int praiseCount = userPraiseRecordService.list(queryWrapper).size();
            newBlogVo.setPraiseCount(praiseCount);
        }

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
