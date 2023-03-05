package cn.lico.geek.modules.blog.service.Impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.entity.BlogSort;
import cn.lico.geek.modules.blog.entity.BlogTag;
import cn.lico.geek.modules.blog.form.BlogForm;
import cn.lico.geek.modules.blog.form.PageVo;
import cn.lico.geek.modules.blog.mapper.BlogMapper;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.modules.blog.service.BlogSortService;
import cn.lico.geek.modules.blog.service.BlogTagService;
import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.blog.vo.BlogInfoVo;
import cn.lico.geek.modules.blog.vo.NewBlogUserVo;
import cn.lico.geek.modules.blog.vo.NewBlogVo;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.queue.service.IMessageQueueService;
import cn.lico.geek.modules.search.entity.SearchItem;
import cn.lico.geek.modules.search.entity.extra.BlogExtra;
import cn.lico.geek.modules.tag.entity.Tag;
import cn.lico.geek.modules.tag.service.TagService;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import cn.lico.geek.modules.user.entity.UserStatistics;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.modules.user.form.UserBlogForm;
import cn.lico.geek.modules.user.vo.UserBlogVo;
import cn.lico.geek.utils.AbstractUtils;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.RedisCache;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private UserStatisticsService userStatisticsService;

    @Autowired
    private IMessageQueueService messageQueueService;

    @Autowired
    private RedisCache redisCache;
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
        queryWrapper.eq(Blog::getStatus,1);
        queryWrapper.eq(Blog::getIsPublish,"1");
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
            if (SecurityUtils.isLogin()){
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
            }else {
                queryWrapper.eq(Blog::getUserUid,"-1");
            }
        }
        //判断blogSortUid是否为空，如果不为空则加入判断条件
        queryWrapper.eq(Objects.nonNull(pageVo.getBlogSortUid())&&pageVo.getBlogSortUid().length()>0,Blog::getBlogSortUid,pageVo.getBlogSortUid());
        //去除1级推荐文章
        queryWrapper.ne(Blog::getLevel,1);
        queryWrapper.eq(Blog::getIsPublish,"1");
        queryWrapper.eq(Blog::getStatus,1);
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
     * 发布博客
     * @param blogForm
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addBlog(BlogForm blogForm) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();
        //根据id获取用户昵称
        String nickName = userService.getById(userId).getNickName();
        //填充blog对象
        Blog blog = new Blog();
        //判空
        if (Objects.nonNull(blogForm.getPhotoList())&&blogForm.getPhotoList().size()>0){
            blog.setPhotoList(blogForm.getPhotoList().get(0));
        }
        if (Objects.isNull(blogForm.getSummary())|| blogForm.getSummary().length() == 0){
            blog.setSummary(AbstractUtils.extractWithoutHtml(blogForm.getContent(),20));
        }else{
            blog.setSummary(blogForm.getSummary());
        }
        blog.setBlogSortUid(blogForm.getBlogSortUid());
        blog.setContent(blogForm.getContent());
        blog.setTitle(blogForm.getTitle());
        blog.setIsPublish(blogForm.getIsPublish());
        blog.setAdminUid(userId);
        blog.setAuthor(nickName);
        blog.setUserUid(userId);
        boolean flag = save(blog);
        if (flag){
            //添加博客标签表
            String uid = blog.getUid();
            String tagUids = blogForm.getTagUid();
            String[] list = tagUids.split(",");
            for (String s : list) {
                BlogTag blogTag = new BlogTag();
                blogTag.setBlogId(uid);
                blogTag.setTagId(s);
                blogTagService.save(blogTag);
            }
            //发送添加博客信息
//            DataItemChangeMessage dataItemChangeMessage = DataItemChangeMessage.addMessage(DataItemType.BLOG, userId);
            DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
            dataItemChangeMessage.setItemId(blog.getUid());
            dataItemChangeMessage.setItemType(DataItemType.BLOG);
            dataItemChangeMessage.setOperatorId(userId);
            dataItemChangeMessage.setChangeType(DataItemChangeType.ADD);
            messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
            return new ResponseResult("发布成功！",AppHttpCodeEnum.SUCCESS.getMsg());
        }
        return new ResponseResult("发布失败，请重新检查！",AppHttpCodeEnum.ERROR.getMsg());
    }

    /**
     *根据博客id获取博文
     * @param oid
     * @param isLazy
     * @return
     */
    @Override
    public ResponseResult getBlogByUid(Integer oid, Integer isLazy,String remoteHost) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        //根据oid进行查询
        queryWrapper.eq(Blog::getOid,oid);
        //判断博客是否有效
        queryWrapper.eq(Blog::getStatus,1);
        //根据条件获取博客
        Blog blog = getOne(queryWrapper);
        //设置博客浏览量
        //根据Ip 地址在 redis查询结果
        Object cacheObject = redisCache.getCacheObject(oid+remoteHost);
        //如果查询结果为空，则新增记录到redids中，并且将该博客浏览量加1
        if (Objects.isNull(cacheObject)){
            //新增记录到redids中
            redisCache.setCacheObject(oid+remoteHost,"1",60*60, TimeUnit.SECONDS);
            //将博客浏览量+1
            blog.setClickCount(blog.getClickCount()+1);
            updateById(blog);

            //发送博客浏览记录消息
            DataItemChangeMessage dataItemChangeMessage  = new DataItemChangeMessage();
            dataItemChangeMessage.setItemId(blog.getUid());
            dataItemChangeMessage.setChangeType(DataItemChangeType.ADD);
            dataItemChangeMessage.setItemType(DataItemType.BLOG_VIEW_NUM);
            dataItemChangeMessage.setOperatorId(blog.getUserUid());
            messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
        }
        BlogInfoVo blogInfoVo = BeanCopyUtils.copyBean(blog, BlogInfoVo.class);
        //获取博文作者id
        String userUid = blogInfoVo.getUserUid();
        //获取博文作者对象
        User user = userService.getById(userUid);
        BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
        //根据userUid获取用户统计信息
        UserStatistics userStatistics = userStatisticsService.getById(userUid);
        //填充用户博客发布量和博客总浏览量
        blogInfoUser.setBlogPublishCount(userStatistics.getBlogNum());
        blogInfoUser.setBlogVisitCount(userStatistics.getBlogViewNum());
        //填充blogInfoVo的user
        blogInfoVo.setUser(blogInfoUser);

        //填充获取博客标签列表
        //根据博客id获取博客标签列表
        String blogUid = blogInfoVo.getUid();
        LambdaQueryWrapper<BlogTag> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(BlogTag::getBlogId,blogUid);
        List<BlogTag> list = blogTagService.list(queryWrapper1);
        List<Tag> tags = new ArrayList<>();
        for (BlogTag blogTag : list) {
            LambdaQueryWrapper<Tag> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(Tag::getUid,blogTag.getTagId());
            Tag tag = tagService.getOne(queryWrapper2);
            tags.add(tag);
        }
        blogInfoVo.setTagList(tags);

        //填充博客分类
        //根据博客分类id获取分类信息
        String blogSortUid = blogInfoVo.getBlogSortUid();
        BlogSort blogSort = blogSortService.getById(blogSortUid);
        blogInfoVo.setBlogSort(blogSort);

        return new ResponseResult(blogInfoVo);
    }


    /**
     * 获取指定用户博客列表
     * @param userBlogForm
     * @return
     */
    @Override
    public ResponseResult getBlogListByUser(UserBlogForm userBlogForm) {
        //进行条件匹配
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Blog::getUserUid,userBlogForm.getUserUid());
        queryWrapper.eq(Blog::getStatus,1);
        if ("create_time".equals(userBlogForm.getOrderByDescColumn())){
            queryWrapper.orderByDesc(Blog::getCreateTime);
        }else {
            queryWrapper.orderByDesc(Blog::getClickCount);
        }
        //进行分页查询
        Page<Blog> page = new Page<>(userBlogForm.getCurrentPage(),userBlogForm.getPageSize());
        page(page,queryWrapper);

        List<UserBlogVo> userBlogVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserBlogVo.class);
        for (UserBlogVo userBlogVo : userBlogVos) {
            //填充分类信息
            Blog blog = getById(userBlogVo.getUid());
            String blogSortUid = blog.getBlogSortUid();
            BlogSort blogSort = blogSortService.getById(blogSortUid);
            userBlogVo.setBlogSort(blogSort);
            //填充标签信息
            //根据blogUid获取BlogTag列表
            LambdaQueryWrapper<BlogTag> queryWrapper1  = new LambdaQueryWrapper<>();
            queryWrapper1.eq(BlogTag::getBlogId,userBlogVo.getUid());
            //获取blogTag列表
            List<BlogTag> blogTags = blogTagService.list(queryWrapper1);
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
            userBlogVo.setTagList(tags);
        }
        PageDTO<UserBlogVo> pageDTO = new PageDTO<>();
        pageDTO.setRecords(userBlogVos);
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setSize((int)page.getSize());
        return new ResponseResult(pageDTO);
    }

    /**
     * 根据标签查询博客列表
     * @param tagUid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult searchBlogByTag(String tagUid, Integer currentPage, Integer pageSize) {
        LambdaQueryWrapper<BlogTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogTag::getTagId,tagUid);
        queryWrapper.eq(BlogTag::getStatus,1);
        List<BlogTag> list = blogTagService.list(queryWrapper);
        List<String> blogIds = new ArrayList<>();
        if (list.size()>0){
            for (BlogTag blogTag : list) {
                blogIds.add(blogTag.getBlogId());
            }
        }
        LambdaQueryWrapper<Blog> queryWrapper1 = new LambdaQueryWrapper<>();
        if (blogIds.size()>0){
            queryWrapper1.in(Blog::getUid,blogIds)
                    .orderByDesc(Blog::getClickCount);
        }else {
            queryWrapper1.eq(Blog::getUid,-1);
        }
        Page<Blog> page = new Page<>(currentPage,pageSize);
        page(page,queryWrapper1);
        List<SearchItem<BlogExtra>> searchItems = new ArrayList<>();
        for (Blog pageRecord : page.getRecords()) {
            //填充分类信息
            LambdaQueryWrapper<BlogSort> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(BlogSort::getUid,pageRecord.getBlogSortUid());
            BlogSort blogSort = blogSortService.getOne(queryWrapper2);
            SearchItem searchItem1 = BeanCopyUtils.copyBean(pageRecord, SearchItem.class);
            BlogExtra blogExtra = new BlogExtra();
            blogExtra.setOid(pageRecord.getOid());
            blogExtra.setIsVip(pageRecord.getIsVip());
            blogExtra.setOutsideLink(pageRecord.getOutsideLink());
            blogExtra.setBlogSortName(blogSort.getSortName());
            blogExtra.setAuthor(pageRecord.getAuthor());
            blogExtra.setBlogSortUid(pageRecord.getBlogSortUid());
            blogExtra.setType(pageRecord.getType());
            searchItem1.setResourceType("BLOG");
            searchItem1.setExtra(blogExtra);
            searchItems.add(searchItem1);
        }
        PageDTO<SearchItem<BlogExtra>> pageDTO = new PageDTO<>();
        pageDTO.setRecords(searchItems);
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setSize((int)page.getSize());
        return new ResponseResult(pageDTO);
    }

    /**
     * 根据博客分类ID查询博客列表
     * @param blogSortUid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult searchBlogBySort(String blogSortUid, Integer currentPage, Integer pageSize) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogSortUid,blogSortUid)
                .eq(Blog::getStatus,1)
                .orderByDesc(Blog::getClickCount);
        Page<Blog> page = new Page<>(currentPage,pageSize);
        page(page,queryWrapper);
        List<SearchItem<BlogExtra>> searchItems = new ArrayList<>();
        for (Blog pageRecord : page.getRecords()) {
            //填充分类信息
            LambdaQueryWrapper<BlogSort> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(BlogSort::getUid,pageRecord.getBlogSortUid());
            BlogSort blogSort = blogSortService.getOne(queryWrapper2);
            SearchItem searchItem1 = BeanCopyUtils.copyBean(pageRecord, SearchItem.class);
            BlogExtra blogExtra = new BlogExtra();
            blogExtra.setOid(pageRecord.getOid());
            blogExtra.setIsVip(pageRecord.getIsVip());
            blogExtra.setOutsideLink(pageRecord.getOutsideLink());
            blogExtra.setBlogSortName(blogSort.getSortName());
            blogExtra.setAuthor(pageRecord.getAuthor());
            blogExtra.setBlogSortUid(pageRecord.getBlogSortUid());
            blogExtra.setType(pageRecord.getType());
            searchItem1.setResourceType("BLOG");
            searchItem1.setExtra(blogExtra);
            searchItems.add(searchItem1);
        }
        PageDTO<SearchItem<BlogExtra>> pageDTO = new PageDTO<>();
        pageDTO.setRecords(searchItems);
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setSize((int)page.getSize());
        return new ResponseResult(pageDTO);
    }

    /**
     * 获取用户博客列表
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getMeBlogList(Integer currentPage, Integer pageSize) {
        //获取当前用户uid
        String userId = SecurityUtils.getUserId();
        //根据条件查询博客
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getUserUid,userId)
                .eq(Blog::getStatus,1);
        Page<Blog> page = new Page<>(currentPage,pageSize);
        page(page, queryWrapper);
        List<UserBlogVo> userBlogVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserBlogVo.class);
        for (UserBlogVo userBlogVo : userBlogVos) {
            //填充分类信息
            Blog blog = getById(userBlogVo.getUid());
            String blogSortUid = blog.getBlogSortUid();
            BlogSort blogSort = blogSortService.getById(blogSortUid);
            userBlogVo.setBlogSort(blogSort);
            //填充标签信息
            //根据blogUid获取BlogTag列表
            LambdaQueryWrapper<BlogTag> queryWrapper1  = new LambdaQueryWrapper<>();
            queryWrapper1.eq(BlogTag::getBlogId,userBlogVo.getUid());
            //获取blogTag列表
            List<BlogTag> blogTags = blogTagService.list(queryWrapper1);
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
            userBlogVo.setTagList(tags);
        }
        PageDTO<UserBlogVo> pageDTO = new PageDTO<>();
        pageDTO.setRecords(userBlogVos);
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setSize((int)page.getSize());
        return new ResponseResult(pageDTO);
    }

    /**
     * 下架博客
     * @param isPublish
     * @param uid
     * @return
     */
    @Override
    public ResponseResult publish(String isPublish, String uid) {
        Blog blog = getById(uid);
        blog.setIsPublish(isPublish);
        //修改博客标签状态
        LambdaQueryWrapper<BlogTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogTag::getBlogId,uid);
        List<BlogTag> list = blogTagService.list(queryWrapper);
        for (BlogTag blogTag : list){
            if ("0".equals(isPublish)){
                blogTag.setStatus(0);
            }else {
                blogTag.setStatus(1);
            }
            blogTagService.updateById(blogTag);
        }
        boolean flag = updateById(blog);
        if (!flag){
            return new ResponseResult().error("下架失败");
        }
        return new ResponseResult(AppHttpCodeEnum.SUCCESS);
    }


    /**
     * 删除博客
     * @param uid
     * @return
     */
    @Override
    public ResponseResult deleteByUid(String uid) {
        Blog blog = getById(uid);
        blog.setStatus(0);
        boolean flag = updateById(blog);
        if (!flag){
            return new ResponseResult().error("删除失败！");
        }
        //删除博客标签表中的信息
        LambdaQueryWrapper<BlogTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogTag::getBlogId,uid);
        blogTagService.remove(queryWrapper);
        return new ResponseResult(AppHttpCodeEnum.SUCCESS);
    }


    /**
     * 填充点赞数量(有bug)
     * @param newBlogVos
     */
    private void getPraiseCount(List<NewBlogVo> newBlogVos) {
        for (NewBlogVo newBlogVo : newBlogVos) {
            LambdaQueryWrapper<UserPraiseRecord> queryWrapper = new LambdaQueryWrapper<>();
            //aa6e71e0c243b921ca657a900b4b442f
            String uid = newBlogVo.getUid();
            //queryWrapper.eq(UserPraiseRecord::getResourceUid,newBlogVo.getUid());
            queryWrapper.eq(UserPraiseRecord::getResourceUid,uid)
                    .eq(UserPraiseRecord::getResourceType,"1")
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
