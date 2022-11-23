package cn.lico.geek.modules.question.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.moment.vo.PraiseInfo;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.question.entity.QuestionTag;
import cn.lico.geek.modules.question.entity.QuestionTags;
import cn.lico.geek.modules.question.form.QuestionAddForm;
import cn.lico.geek.modules.question.form.QuestionListForm;
import cn.lico.geek.modules.question.mapper.QuestionMapper;
import cn.lico.geek.modules.question.service.QuestionService;
import cn.lico.geek.modules.question.service.QuestionTagService;
import cn.lico.geek.modules.question.service.QuestionTagsService;
import cn.lico.geek.modules.question.vo.QuestionListVo;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import cn.lico.geek.utils.AbstractUtils;
import cn.lico.geek.utils.BeanCopyUtils;
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
        return new ResponseResult("发布成功！", AppHttpCodeEnum.SUCCESS.getMsg());
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