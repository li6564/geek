package cn.lico.geek.modules.question.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.moment.vo.PraiseInfo;
import cn.lico.geek.modules.question.dto.QuestionInfoDto;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.question.entity.QuestionTag;
import cn.lico.geek.modules.question.entity.QuestionTags;
import cn.lico.geek.modules.question.form.QuestionAddForm;
import cn.lico.geek.modules.question.form.QuestionInfoForm;
import cn.lico.geek.modules.question.form.QuestionListForm;
import cn.lico.geek.modules.question.mapper.QuestionMapper;
import cn.lico.geek.modules.question.service.QuestionService;
import cn.lico.geek.modules.question.service.QuestionTagService;
import cn.lico.geek.modules.question.service.QuestionTagsService;
import cn.lico.geek.modules.question.vo.QuestionListVo;
import cn.lico.geek.modules.queue.service.IMessageQueueService;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import cn.lico.geek.modules.user.form.UserBlogForm;
import cn.lico.geek.utils.AbstractUtils;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.RedisCache;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author：linan
 * @Date：2022/11/21 20:25
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Autowired
    private QuestionTagService questionTagService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPraiseRecordService userPraiseRecordService;

    @Autowired
    private QuestionTagsService questionTagsService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IMessageQueueService messageQueueService;

    /**
     * 获取问答列表
     * @param questionListForm
     * @return
     */
    @Override
    public ResponseResult getList(QuestionListForm questionListForm) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        //如果MethodType()不为空则根据MethodType()字段排序
        if (Objects.nonNull(questionListForm.getMethodType())&&questionListForm.getMethodType().length()>0){
            String methodType = questionListForm.getMethodType();
            if ("newQuestion".equals(methodType)){
                queryWrapper.orderByDesc(Question::getCreateTime);
            }else if ("hotQuestion".equals(methodType)){
                queryWrapper.orderByDesc(Question::getClickCount);
            }else if("waitQuestion".equals(methodType)){
                queryWrapper.eq(Question::getReplyCount,0);
            }
        }else {
            queryWrapper.orderByDesc(Question::getClickCount);
        }
        //判断问答是否有效
        queryWrapper.eq(Question::getStatus,1);
        //根据分页查询
        Page<Question> page = new Page<>(questionListForm.getCurrentPage(),questionListForm.getPageSize());
        page(page,queryWrapper);
        //将查询到的问答列表信息转换为需要返回的格式
        List<QuestionListVo> questionListVos = BeanCopyUtils.copyBeanList(page.getRecords(), QuestionListVo.class);
        //为每个问答填充信息
        for (QuestionListVo questionListVo : questionListVos) {
            //填充问答标签
            getQuestionTag(questionListVo.getUid(),questionListVo);
            //填充user信息
            getInfoUser(questionListVo.getUserUid(),questionListVo);
            //填充点赞信息
            getInfoPraise(questionListVo.getUid(),questionListVo);

        }
        PageDTO<QuestionListVo> pageDTO = new PageDTO<>();
        pageDTO.setRecords(questionListVos);
        pageDTO.setSize((int)page.getSize());
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setTotal((int)page.getTotal());

        return new ResponseResult(pageDTO);
    }

    /**
     * 发布问答
     * @param questionAddForm
     * @return
     */
    @Override
    @Transactional
    public ResponseResult add(QuestionAddForm questionAddForm) {
        //保存问答
        Question question = BeanCopyUtils.copyBean(questionAddForm, Question.class);
        //如果问答简介为空，则从问答内容中提取20个字作为简介
        if (Objects.isNull(questionAddForm.getSummary())|| questionAddForm.getSummary().length()==0){
            question.setSummary(AbstractUtils.extractWithoutHtml(questionAddForm.getContent(),20));
        }
        //获取当前用户uid
        String userId = SecurityUtils.getUserId();
        question.setUserUid(userId);
        boolean flag = save(question);
        if (!flag){
            return new ResponseResult("发布失败！",AppHttpCodeEnum.ERROR.getMsg());
        }
        //获取问答uid
        String uid = question.getUid();
        //增加问答标签表
        String[] split = questionAddForm.getQuestionTagUid().split(",");
        for (String s : split) {
            QuestionTags questionTags = new QuestionTags();
            questionTags.setQuestionUid(uid);
            questionTags.setTagUid(s);
            questionTagsService.save(questionTags);
        }
        //发送问答新增消息
        DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
        dataItemChangeMessage.setItemId(uid);
        dataItemChangeMessage.setOperatorId(userId);
        dataItemChangeMessage.setItemType(DataItemType.QUESTION);
        dataItemChangeMessage.setChangeType(DataItemChangeType.ADD);
        messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
        return new ResponseResult("发布成功！", AppHttpCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 获取问答详情
     * @param questionInfoForm
     * @return
     */
    @Override
    public ResponseResult getQuestion(String remoteHost,QuestionInfoForm questionInfoForm) {
        //根据oid获取Question对象
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getOid,questionInfoForm.getOid());
        Question question = getOne(queryWrapper);
        //判断是否重复访问
        Object cacheObject = redisCache.getCacheObject(question.getUid() + remoteHost);
        //如果没有重复访问则将用户IP地址存入redis中
        if (Objects.isNull(cacheObject)){
            redisCache.setCacheObject(question.getUid()+remoteHost,1,60*60, TimeUnit.SECONDS);
            question.setClickCount(question.getClickCount()+1);
            updateById(question);
//            //发送问答阅读消息
//            DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
//            dataItemChangeMessage.setItemId(question.getUid()).setItemType(DataItemType.QUESTION);
//            messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
        }
        //将question转化为返回形式
        QuestionInfoDto questionInfoDto = BeanCopyUtils.copyBean(question, QuestionInfoDto.class);
        //获取问答uid
        String uid = questionInfoDto.getUid();
        //填充点赞信息
        LambdaQueryWrapper<UserPraiseRecord> queryWrapper3 = new LambdaQueryWrapper<>();
        queryWrapper3.eq(UserPraiseRecord::getResourceUid,uid);
        queryWrapper3.eq(UserPraiseRecord::getStatus,1);
        int count = userPraiseRecordService.count(queryWrapper3);
        questionInfoDto.setCollectCount(count);
        //填充标签
        LambdaQueryWrapper<QuestionTags> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(QuestionTags::getQuestionUid,uid);
        List<QuestionTags> questionTagsList = questionTagsService.list(queryWrapper1);
        List<QuestionTag> list = new ArrayList<>();
        for (QuestionTags questionTags : questionTagsList) {
            LambdaQueryWrapper<QuestionTag> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(QuestionTag::getUid,questionTags.getTagUid());
            QuestionTag tag = questionTagService.getOne(queryWrapper2);
            list.add(tag);
        }
        questionInfoDto.setQuestionTag(list);
        //填充作者信息
        User user = userService.getById(questionInfoDto.getUserUid());
        BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
        questionInfoDto.setUser(blogInfoUser);

        return new ResponseResult(questionInfoDto);
    }

    /**
     * 获取指定用户问答列表
     * @param userBlogForm
     * @return
     */
    @Override
    public ResponseResult getQuestionListByUser(UserBlogForm userBlogForm) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getUserUid,userBlogForm.getUserUid());
        queryWrapper.eq(Question::getStatus,1);
        if ("create_time".equals(userBlogForm.getOrderByDescColumn())){
            queryWrapper.orderByDesc(Question::getCreateTime);
        }else {
            queryWrapper.orderByDesc(Question::getClickCount);
        }
        //进行分页查询
        Page<Question> page = new Page<>(userBlogForm.getCurrentPage(),userBlogForm.getPageSize());
        page(page,queryWrapper);
        List<QuestionListVo> questionListVos = BeanCopyUtils.copyBeanList(page.getRecords(), QuestionListVo.class);
        for (QuestionListVo questionListVo : questionListVos) {
            getInfoUser(questionListVo.getUserUid(),questionListVo);
            getQuestionTag(questionListVo.getUid(),questionListVo);
        }
        PageDTO<QuestionListVo> pageDTO = new PageDTO<>();
        pageDTO.setRecords(questionListVos);
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setSize((int)page.getSize());
        pageDTO.setTotal((int)page.getTotal());

        return new ResponseResult(pageDTO);
    }

    @Override
    public ResponseResult getMeQuestionList(Integer currentPage, Integer pageSize) {
        //获取当前用户uid
        String userId = SecurityUtils.getUserId();
        //根据用户uid查询问答
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getUserUid,userId)
                .eq(Question::getStatus,1);
        Page<Question> page = new Page<>(currentPage,pageSize);
        page(page,queryWrapper);
        List<QuestionListVo> questionListVos = BeanCopyUtils.copyBeanList(page.getRecords(), QuestionListVo.class);
        for (QuestionListVo questionListVo : questionListVos) {
            getInfoUser(questionListVo.getUserUid(),questionListVo);
            getQuestionTag(questionListVo.getUid(),questionListVo);
        }
        PageDTO<QuestionListVo> pageDTO = new PageDTO<>();
        pageDTO.setRecords(questionListVos);
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setSize((int)page.getSize());
        pageDTO.setTotal((int)page.getTotal());

        return new ResponseResult(pageDTO);
    }

    /**
     * 删除问答
     * @param uid
     * @return
     */
    @Override
    public ResponseResult delete(String uid) {
        Question question = getById(uid);
        question.setStatus(0);
        boolean flag = updateById(question);
        if (!flag){
            return new ResponseResult().error("删除失败！");
        }
        //删除问答标签表信息
        LambdaQueryWrapper<QuestionTags> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestionTags::getQuestionUid,uid);
        boolean b = questionTagsService.remove(queryWrapper);
        return new ResponseResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 填充点赞信息
     * @param uid
     * @param questionListVo
     */
    private void getInfoPraise(String uid, QuestionListVo questionListVo) {
        PraiseInfo praiseInfo = new PraiseInfo();
        //获取当前登录用户id
        if (SecurityUtils.isLogin()){
            String userId = SecurityUtils.getUserId();
            LambdaQueryWrapper<UserPraiseRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserPraiseRecord::getResourceType,uid);
            queryWrapper.eq(UserPraiseRecord::getStatus,1);
            //获取点赞数量
            int count = userPraiseRecordService.count(queryWrapper);
            queryWrapper.eq(UserPraiseRecord::getUserUid,userId);
            UserPraiseRecord userPraiseRecord = userPraiseRecordService.getOne(queryWrapper);
            //设置问答点赞的数量
            praiseInfo.setPraiseCount(count);
            if (Objects.isNull(userPraiseRecord)){
                praiseInfo.setPraise(false);
            }else {
                praiseInfo.setPraise(true);
            }
        }else {
            praiseInfo.setPraise(false);
            praiseInfo.setPraiseCount(0);
        }

        questionListVo.setPraiseInfo(praiseInfo);
    }

    /**
     * 填充user信息
     * @param userUid
     * @param questionListVo
     */
    private void getInfoUser(String userUid, QuestionListVo questionListVo) {
        BlogInfoUser blogInfoUser = null;
        User user = userService.getById(userUid);
        if (Objects.nonNull(user)){
            blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
        }else {
            System.out.println("没找到user");
        }

        questionListVo.setUser(blogInfoUser);
    }

    /**
     * 填充问答标签
     * @param uid
     * @param questionListVo
     */
    private void getQuestionTag(String uid, QuestionListVo questionListVo) {
        LambdaQueryWrapper<QuestionTags> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestionTags::getQuestionUid,uid);
        List<QuestionTags> questionTags = questionTagsService.list(queryWrapper);
        List<QuestionTag> list = new ArrayList<>();
        for (QuestionTags questionTag : questionTags) {
            LambdaQueryWrapper<QuestionTag> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(QuestionTag::getUid,questionTag.getTagUid());
            queryWrapper1.eq(QuestionTag::getStatus,1);
            QuestionTag questionTag1 = questionTagService.getOne(queryWrapper1);
            list.add(questionTag1);
        }
        questionListVo.setQuestionTagList(list);
    }
}
