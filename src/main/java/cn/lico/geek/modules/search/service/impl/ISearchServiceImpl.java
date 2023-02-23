package cn.lico.geek.modules.search.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.modules.blog.service.BlogSortService;
import cn.lico.geek.modules.moment.entity.MomentPhoto;
import cn.lico.geek.modules.moment.entity.MomentTopic;
import cn.lico.geek.modules.moment.entity.UserMoment;
import cn.lico.geek.modules.moment.entity.UserMomentTopic;
import cn.lico.geek.modules.moment.service.MomentPhotoService;
import cn.lico.geek.modules.moment.service.MomentTopicService;
import cn.lico.geek.modules.moment.service.UserMomentService;
import cn.lico.geek.modules.moment.service.UserMomentTopicService;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.question.entity.QuestionTag;
import cn.lico.geek.modules.question.entity.QuestionTags;
import cn.lico.geek.modules.question.service.QuestionService;
import cn.lico.geek.modules.question.service.QuestionTagService;
import cn.lico.geek.modules.question.service.QuestionTagsService;
import cn.lico.geek.modules.search.entity.SearchItem;
import cn.lico.geek.modules.search.entity.UserInfo;
import cn.lico.geek.modules.search.entity.extra.BlogExtra;
import cn.lico.geek.modules.search.entity.extra.MomentExtra;
import cn.lico.geek.modules.search.entity.extra.QuestionExtra;
import cn.lico.geek.modules.search.entity.extra.UserExtra;
import cn.lico.geek.modules.search.repository.ISearchRepository;
import cn.lico.geek.modules.search.service.ISearchService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserStatistics;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author：linan
 * @Date：2022/12/4 12:34
 */
@Service
@Slf4j
public class ISearchServiceImpl implements ISearchService {
    @Autowired
    private ISearchRepository searchRepository;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserMomentService momentService;

    @Autowired
    private BlogSortService blogSortService;

    @Autowired
    private UserStatisticsService userStatisticsService;

    @Autowired
    private UserWatchService userWatchService;

    @Autowired
    private QuestionTagService questionTagService;

    @Autowired
    private QuestionTagsService questionTagsService;

    @Autowired
    private UserMomentTopicService userMomentTopicService;

    @Autowired
    private MomentTopicService momentTopicService;

    @Autowired
    private MomentPhotoService momentPhotoService;

    @Override
    public void saveSearchItem(DataItemType itemType, String itemId) {
        SearchItem searchItem = getSearchItemInDb(itemType, itemId);
        if (Objects.nonNull(searchItem)){
            searchRepository.save(searchItem);
            log.debug("elasticsearch-save:{}", searchItem);
        }
    }

    @Override
    public void deleteSearchItem(DataItemType dataItemType, String itemId) {
        searchRepository.deleteById(itemId);
        log.debug("elasticsearch-delete:{}", itemId);
    }

    @Override
    public void updateSearchItem(DataItemType dataItemType, String itemId) {
        SearchItem searchItem = getSearchItemInDb(dataItemType, itemId);
        searchRepository.save(searchItem);
        log.debug("elasticsearch-update:{}", searchItem);
    }

    @Override
    public Optional<SearchItem> getSearchItem(DataItemType dataItemType, String itemId) {
        return searchRepository.findById(itemId);
    }


    @Override
    public ResponseResult searchPage(int currentPage, Integer pageSize, String keywords, String resourceType) {
        // 构造搜索条件
        DisMaxQueryBuilder disMaxQueryBuilder = new DisMaxQueryBuilder();
        QueryBuilder titleQueryBuilder = QueryBuilders.matchPhraseQuery("title", keywords).boost(2f);
        QueryBuilder descriptionQueryBuilder = QueryBuilders.matchPhraseQuery("summary", keywords);
        QueryBuilder contentQueryBuilder = QueryBuilders.matchPhraseQuery("content", keywords);
        disMaxQueryBuilder.add(titleQueryBuilder);
        disMaxQueryBuilder.add(descriptionQueryBuilder);
        disMaxQueryBuilder.add(contentQueryBuilder);

        BoolQueryBuilder boolQueryBuilder
                = QueryBuilders
                .boolQuery()
                .must(disMaxQueryBuilder);

        if (Objects.nonNull(resourceType)) {
            boolQueryBuilder.must(QueryBuilders.constantScoreQuery(QueryBuilders.matchPhraseQuery("resourceType", resourceType)));
        }
        //搜索
        Page<SearchItem> searchItemPage = searchRepository.search(boolQueryBuilder, PageRequest.of(currentPage, pageSize, Sort.by(Sort.Order.desc("createTime"))));

        if ("USER".equals(resourceType)){
            for (SearchItem searchItem : searchItemPage.getContent()) {
                Object extra = searchItem.getExtra();
                UserExtra userExtra = JSON.parseObject(JSON.toJSONString(extra), UserExtra.class);
                UserStatistics userStatistics = userStatisticsService.getById(searchItem.getUid());
                userExtra.setBlogPublishCount(userStatistics.getBlogNum());
                userExtra.setUserMomentCount(userStatistics.getPostNum());
                LambdaQueryWrapper<UserWatch> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(UserWatch::getUserUid,searchItem.getUid())
                        .eq(UserWatch::getStatus,1);
                int count = userWatchService.count(queryWrapper);
                userExtra.setUserWatchCount(count);
                if (!SecurityUtils.isLogin()){
                    userExtra.setUserWatchStatus(false);
                }else {
                    String userId = SecurityUtils.getUserId();
                    LambdaQueryWrapper<UserWatch> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(UserWatch::getUserUid,userId)
                            .eq(UserWatch::getToUserUid,searchItem.getUid())
                            .eq(UserWatch::getStatus,1);
                    UserWatch userWatch = userWatchService.getOne(queryWrapper1);
                    if (Objects.nonNull(userWatch)){
                        userExtra.setUserWatchStatus(true);
                    }else {
                        userExtra.setUserWatchStatus(false);
                    }
                }
                searchItem.setExtra(userExtra);
            }
        }else if ("QUESTION".equals(resourceType)){
            for (SearchItem searchItem : searchItemPage.getContent()) {
                Object extra = searchItem.getExtra();
                QuestionExtra questionExtra = JSON.parseObject(JSON.toJSONString(extra), QuestionExtra.class);
                Question question = questionService.getById(searchItem.getUid());
                questionExtra.setReplyCount(question.getReplyCount());
                searchItem.setClickCount(question.getClickCount());
                searchItem.setExtra(questionExtra);
            }
        }
        PageDTO<SearchItem> pageDTO = new PageDTO<>();
        pageDTO.setRecords(searchItemPage.getContent());
        pageDTO.setTotal(searchItemPage.getTotalPages());
        pageDTO.setCurrent(currentPage+1);
        pageDTO.setSize(searchItemPage.getSize());
        return new ResponseResult(pageDTO);
    }

    private SearchItem getSearchItemInDb(DataItemType itemType, String itemId) {
        SearchItem searchItem = null;
        try {
            switch (itemType){
                case BLOG:
                    searchItem = buildSearchItemFromBlog(itemId);
                    System.out.println("blog1");
                    break;
                case USER:
                    searchItem = buildSearchItemFromUser(itemId);
                    System.out.println("USER");
                    break;
                case QUESTION:
                    searchItem = buildSearchItemFromQuestion(itemId);
                    System.out.println("QUESTION");
                    break;
                case QUESTION_REPLY:
                    searchItem = buildSearchItemFromQuestion(itemId);
                    break;
                case POST:
                    searchItem = buildSearchItemFromTopic(itemId);
                    break;
                case BLOG_VIEW_NUM:
                    searchItem = buildSearchItemFromBlog(itemId);
                    break;
                default:
                    throw new IllegalArgumentException("该类型不支持搜索："+ itemType.name() + ":" + itemId);
            }
        }catch (Exception e){
            log.error("搜索条目存储失败:{}", e.getMessage());
            throw e;
        }
        return searchItem;
    }

    private SearchItem buildSearchItemFromTopic(String itemId) {
        UserMoment userMoment = momentService.getById(itemId);
        if (Objects.nonNull(userMoment)) {
            SearchItem searchItem = new SearchItem(itemId, "MOMENT");
            searchItem.setUid(userMoment.getUid());
            searchItem.setContent(userMoment.getContent());
            searchItem.setCreateTime(userMoment.getCreateTime());
            searchItem.setUserUid(userMoment.getUserUid());
            MomentExtra momentExtra = new MomentExtra();
            momentExtra.setUrl(userMoment.getUrl());
            momentExtra.setUrlInfo(userMoment.getUrlInfo());
            //填充话题信息
            LambdaQueryWrapper<MomentTopic> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MomentTopic::getMomentUid, userMoment.getUid());
            List<MomentTopic> list = momentTopicService.list(queryWrapper);
            List<String> topicIds = new ArrayList<>();
            if (list.size()>0) {
                for (MomentTopic momentTopic : list) {
                    topicIds.add(momentTopic.getTopicUid());
                }
            }
            if (topicIds.size()>0) {
                LambdaQueryWrapper<UserMomentTopic> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.in(UserMomentTopic::getUid, topicIds);
                List<UserMomentTopic> list1 = userMomentTopicService.list(queryWrapper1);
                momentExtra.setUserMomentTopicList(list1);
            }
            //填充User信息
            User user = userService.getById(userMoment.getUserUid());
            UserInfo userInfo = BeanCopyUtils.copyBean(user, UserInfo.class);
            momentExtra.setUser(userInfo);
            //填充图片
            LambdaQueryWrapper<MomentPhoto> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(MomentPhoto::getMomentUid,userMoment.getUid());
            List<MomentPhoto> list1 = momentPhotoService.list(queryWrapper1);
            List<String> photoList = new ArrayList<>();
            for (MomentPhoto momentPhoto : list1) {
                photoList.add(momentPhoto.getPhotoUrl());
            }
            momentExtra.setPhotoList(photoList);
            searchItem.setExtra(momentExtra);
            return searchItem;
        }
        return null;
    }

    private SearchItem buildSearchItemFromQuestion(String itemId) {
        Question question = questionService.getById(itemId);
        if (Objects.nonNull(question)){
            SearchItem searchItem = new SearchItem(itemId,"QUESTION");
            //searchItem.setClickCount(question.getClickCount());
            searchItem.setTitle(question.getTitle());
            searchItem.setCreateTime(question.getCreateTime());
            searchItem.setUid(question.getUid());
            searchItem.setUserUid(question.getUserUid());
            searchItem.setContent(question.getContent());
            QuestionExtra questionExtra = new QuestionExtra();
            //填充问答标签
            LambdaQueryWrapper<QuestionTags> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(QuestionTags::getQuestionUid,question.getUid());
            List<QuestionTags> list = questionTagsService.list(queryWrapper);
            List<String> tagIds = new ArrayList<>();
            if (Objects.nonNull(list)&&list.size()>0){
                for (QuestionTags questionTags : list) {
                    tagIds.add(questionTags.getTagUid());
                }
            }
            LambdaQueryWrapper<cn.lico.geek.modules.question.entity.QuestionTag> queryWrapper1 = new LambdaQueryWrapper<>();
            if (Objects.nonNull(tagIds)&&tagIds.size()>0){
                queryWrapper1.in(QuestionTag::getUid,tagIds);
                List<QuestionTag> list1 = questionTagService.list(queryWrapper1);
                questionExtra.setQuestionTagList(list1);
            }
            User user = userService.getById(question.getUserUid());
            UserInfo userInfo = BeanCopyUtils.copyBean(user, UserInfo.class);
            questionExtra.setUser(userInfo);
            //questionExtra.setReplyCount(question.getReplyCount());
            questionExtra.setOid(question.getOid());
            searchItem.setExtra(questionExtra);
            //System.out.println(searchItem.toString());
            return searchItem;
        }
        return null;
    }

    private SearchItem buildSearchItemFromUser(String itemId) {
        User user = userService.getById(itemId);
        if (Objects.nonNull(user)){
            SearchItem searchItem = new SearchItem(itemId,"USER");
            searchItem.setPhotoUrl(user.getPhotoUrl());
            searchItem.setSummary(user.getSummary());
            searchItem.setCreateTime(user.getCreateTime());
            searchItem.setUid(user.getUid());
            searchItem.setUserUid(user.getUid());
            searchItem.setContent(user.getNickName());
            UserExtra userExtra = new UserExtra();
            userExtra.setNickName(user.getNickName());
            userExtra.setGender(user.getGender());
            userExtra.setOccupation(user.getOccupation());
            userExtra.setUserTag(user.getUserTag());
            searchItem.setExtra(userExtra);
            //System.out.println(searchItem.toString());
            return searchItem;
        }
        return null;
    }

    private SearchItem buildSearchItemFromBlog(String itemId) {
        System.out.println("buildSearchItemFromBlog");
        Blog blog = blogService.getById(itemId);
        if (Objects.nonNull(blog)){
            SearchItem searchItem = new SearchItem(itemId,"BLOG");
            searchItem.setContent(blog.getContent());
            searchItem.setCreateTime(blog.getCreateTime());
            searchItem.setPhotoUrl(blog.getPhotoList());
            searchItem.setSummary(blog.getSummary());
            searchItem.setTitle(blog.getTitle());
            searchItem.setUid(blog.getUid());
            searchItem.setUserUid(blog.getUserUid());
            searchItem.setClickCount(blog.getClickCount());
            searchItem.setCollectCount(blog.getCollectCount());
            BlogExtra blogExtra = new BlogExtra();
            blogExtra.setAuthor(blog.getAuthor());
            blogExtra.setBlogSortUid(blog.getBlogSortUid());
            String sortName = blogSortService.getById(blog.getBlogSortUid()).getSortName();
            blogExtra.setBlogSortName(sortName);
            blogExtra.setIsVip(blog.getIsVip());
            blogExtra.setOid(blog.getOid());
            blogExtra.setOutsideLink(blog.getOutsideLink());
            blogExtra.setType(blog.getType());
            searchItem.setExtra(blogExtra);

            //System.out.println(searchItem.toString());
            return searchItem;
        }
        return null;
    }
}
