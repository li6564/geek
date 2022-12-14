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
 * @Author???linan
 * @Date???2022/11/8 11:56
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
     * ????????????????????????????????????
     * @param currentPage
     * @param pageSize
     * @param level
     * @param useSort
     * @return
     */
    @Override
    public ResponseResult getBlogByLevel(Integer currentPage, Integer pageSize, Integer level, Integer useSort) {
        //??????level??????????????????
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getLevel,level);
        //?????????????????????
        PageDTO<NewBlogVo> pageDTO = new PageDTO<>();
        if (Objects.nonNull(currentPage)&&Objects.nonNull(pageSize)){
            //????????????????????? pageSize ?????????
            Page<Blog> page = new Page<>(currentPage,pageSize);
            page(page,queryWrapper);
            //????????????????????????
            List<NewBlogVo> newBlogVos = BeanCopyUtils.copyBeanList(page.getRecords(), NewBlogVo.class);
            pageDTO.setRecords(newBlogVos);
            pageDTO.setCurrent((int)page.getCurrent());
            pageDTO.setTotal((int)page.getTotal());
            pageDTO.setSize((int)page.getSize());
        }else {//??????????????????
            //???????????????????????????
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
     * ????????????id??????????????????
     * @param pageVo
     * @return
     */
    @Override
    public ResponseResult getNewBlog(PageVo pageVo) {
        //??????????????????
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(pageVo.getOrderBy())&&pageVo.getOrderBy().length()>0){
            //????????????id
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
        //??????blogSortUid???????????????????????????????????????????????????
        queryWrapper.eq(Objects.nonNull(pageVo.getBlogSortUid())&&pageVo.getBlogSortUid().length()>0,Blog::getBlogSortUid,pageVo.getBlogSortUid());
        //??????????????????
        if ("create_time".equals(pageVo.getOrderByDescColumn())){
            queryWrapper.orderByDesc(Blog::getCreateTime);
        }else if ("click_count".equals(pageVo.getOrderByDescColumn())){
            queryWrapper.orderByDesc(Blog::getClickCount);
        }else {
            queryWrapper.orderByDesc(Blog::getCreateTime);
        }


        Page<Blog> page  = new Page<>(pageVo.getCurrentPage(),pageVo.getPageSize());
        page(page, queryWrapper);

        //???pages?????????????????????NewBlogVo???
        List<NewBlogVo> newBlogVos = BeanCopyUtils.copyBeanList(page.getRecords(), NewBlogVo.class);

        //??????????????????
        getPraiseCount(newBlogVos);
        //??????uer??????
        getUserInformation(newBlogVos);
        //?????? ????????????
        getTagInformation(newBlogVos);
        //??????????????????
        getSortInformation(newBlogVos);

        PageDTO<NewBlogVo> pageDto = new PageDTO<>();
        pageDto.setRecords(newBlogVos);
        pageDto.setCurrent((int)page.getCurrent());
        pageDto.setTotal((int)page.getTotal());
        pageDto.setSize((int)page.getSize());

        return new ResponseResult(pageDto);
    }

    /**
     * ????????????
     * @param blogForm
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addBlog(BlogForm blogForm) {
        //??????????????????id
        String userId = SecurityUtils.getUserId();
        //??????id??????????????????
        String nickName = userService.getById(userId).getNickName();
        //??????blog??????
        Blog blog = new Blog();
        //??????
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
            //?????????????????????
            String uid = blog.getUid();
            String tagUids = blogForm.getTagUid();
            String[] list = tagUids.split(",");
            for (String s : list) {
                BlogTag blogTag = new BlogTag();
                blogTag.setBlogId(uid);
                blogTag.setTagId(s);
                blogTagService.save(blogTag);
            }
            //????????????????????????
//            DataItemChangeMessage dataItemChangeMessage = DataItemChangeMessage.addMessage(DataItemType.BLOG, userId);
            DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
            dataItemChangeMessage.setItemId(blog.getUid());
            dataItemChangeMessage.setItemType(DataItemType.BLOG);
            dataItemChangeMessage.setOperatorId(userId);
            dataItemChangeMessage.setChangeType(DataItemChangeType.ADD);
            messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
            return new ResponseResult("???????????????",AppHttpCodeEnum.SUCCESS.getMsg());
        }
        return new ResponseResult("?????????????????????????????????",AppHttpCodeEnum.ERROR.getMsg());
    }

    /**
     *????????????id????????????
     * @param oid
     * @param isLazy
     * @return
     */
    @Override
    public ResponseResult getBlogByUid(Integer oid, Integer isLazy,String remoteHost) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        //??????oid????????????
        queryWrapper.eq(Blog::getOid,oid);
        //????????????????????????
        queryWrapper.eq(Blog::getStatus,1);
        //????????????????????????
        Blog blog = getOne(queryWrapper);
        //?????????????????????
        //??????Ip ????????? redis????????????
        Object cacheObject = redisCache.getCacheObject(oid+remoteHost);
        //?????????????????????????????????????????????redids????????????????????????????????????1
        if (Objects.isNull(cacheObject)){
            //???????????????redids???
            redisCache.setCacheObject(oid+remoteHost,"1",60*60, TimeUnit.SECONDS);
            //??????????????????+1
            blog.setClickCount(blog.getClickCount()+1);
            updateById(blog);

            //??????????????????????????????
            DataItemChangeMessage dataItemChangeMessage  = new DataItemChangeMessage();
            dataItemChangeMessage.setItemId(blog.getUid());
            dataItemChangeMessage.setChangeType(DataItemChangeType.ADD);
            dataItemChangeMessage.setItemType(DataItemType.BLOG_VIEW_NUM);
            dataItemChangeMessage.setOperatorId(blog.getUserUid());
            messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
        }
        BlogInfoVo blogInfoVo = BeanCopyUtils.copyBean(blog, BlogInfoVo.class);
        //??????????????????id
        String userUid = blogInfoVo.getUserUid();
        //????????????????????????
        User user = userService.getById(userUid);
        BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
        //??????userUid????????????????????????
        UserStatistics userStatistics = userStatisticsService.getById(userUid);
        //????????????????????????????????????????????????
        blogInfoUser.setBlogPublishCount(userStatistics.getBlogNum());
        blogInfoUser.setBlogVisitCount(userStatistics.getBlogViewNum());
        //??????blogInfoVo???user
        blogInfoVo.setUser(blogInfoUser);

        //??????????????????????????????
        //????????????id????????????????????????
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

        //??????????????????
        //??????????????????id??????????????????
        String blogSortUid = blogInfoVo.getBlogSortUid();
        BlogSort blogSort = blogSortService.getById(blogSortUid);
        blogInfoVo.setBlogSort(blogSort);

        return new ResponseResult(blogInfoVo);
    }


    /**
     * ??????????????????????????????
     * @param userBlogForm
     * @return
     */
    @Override
    public ResponseResult getBlogListByUser(UserBlogForm userBlogForm) {
        //??????????????????
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Blog::getUserUid,userBlogForm.getUserUid());
        if ("create_time".equals(userBlogForm.getOrderByDescColumn())){
            queryWrapper.orderByDesc(Blog::getCreateTime);
        }else {
            queryWrapper.orderByDesc(Blog::getClickCount);
        }
        //??????????????????
        Page<Blog> page = new Page<>(userBlogForm.getCurrentPage(),userBlogForm.getPageSize());
        page(page,queryWrapper);

        List<UserBlogVo> userBlogVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserBlogVo.class);
        for (UserBlogVo userBlogVo : userBlogVos) {
            //??????????????????
            Blog blog = getById(userBlogVo.getUid());
            String blogSortUid = blog.getBlogSortUid();
            BlogSort blogSort = blogSortService.getById(blogSortUid);
            userBlogVo.setBlogSort(blogSort);
            //??????????????????
            //??????blogUid??????BlogTag??????
            LambdaQueryWrapper<BlogTag> queryWrapper1  = new LambdaQueryWrapper<>();
            queryWrapper1.eq(BlogTag::getBlogId,userBlogVo.getUid());
            //??????blogTag??????
            List<BlogTag> blogTags = blogTagService.list(queryWrapper1);
            //??????tag id??????
            List<String> TagIds = new ArrayList<>();
            for (BlogTag blogTag : blogTags) {
                TagIds.add(blogTag.getTagId());
            }
            List<Tag> tags = new ArrayList<>();
            //??????tagid?????? ????????????
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
     * ??????????????????????????????
     * @param tagUid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult searchBlogByTag(String tagUid, Integer currentPage, Integer pageSize) {
        LambdaQueryWrapper<BlogTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogTag::getTagId,tagUid);
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
            //??????????????????
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
     * ??????????????????ID??????????????????
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
            //??????????????????
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
     * ??????????????????(???bug)
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
     * ??????????????????
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
     * ?????? ????????????
     * @param newBlogVos
     */
    private void getTagInformation(List<NewBlogVo> newBlogVos) {

        for (NewBlogVo newBlogVo : newBlogVos) {
            //??????blogUid??????BlogTag??????
            LambdaQueryWrapper<BlogTag> queryWrapper  = new LambdaQueryWrapper<>();
            queryWrapper.eq(BlogTag::getBlogId,newBlogVo.getUid());
            //??????blogTag??????
            List<BlogTag> blogTags = blogTagService.list(queryWrapper);
            //??????tag id??????
            List<String> TagIds = new ArrayList<>();
            for (BlogTag blogTag : blogTags) {
                TagIds.add(blogTag.getTagId());
            }
            List<Tag> tags = new ArrayList<>();
            //??????tagid?????? ????????????
            if (TagIds.size()!=0){
                tags = tagService.listByIds(TagIds);
            }
            newBlogVo.setTagList(tags);
        }
    }

    /**
     * ??????uer??????
     * @param newBlogVos
     */
    public void getUserInformation(List<NewBlogVo> newBlogVos){
        //??????newBlogVos????????????newBlogVos??????user??????
        for (NewBlogVo newBlogVo : newBlogVos) {
            //????????????
            Blog blog = getById(newBlogVo.getUid());
            //??????userid
            String userUid = blog.getUserUid();
            //??????user
            User user = userService.getById(userUid);
            //???uer????????????????????????
            NewBlogUserVo newBlogUserVo = null;
            if (Objects.nonNull(user)){
                newBlogUserVo = BeanCopyUtils.copyBean(user, NewBlogUserVo.class);
            }
            //??????user??????
            newBlogVo.setUser(newBlogUserVo);
        }
    }
}
