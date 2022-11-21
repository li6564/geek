package cn.lico.geek.modules.question.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.moment.vo.PraiseInfo;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.question.entity.QuestionTag;
import cn.lico.geek.modules.question.form.QuestionListForm;
import cn.lico.geek.modules.question.mapper.QuestionMapper;
import cn.lico.geek.modules.question.service.QuestionService;
import cn.lico.geek.modules.question.service.QuestionTagService;
import cn.lico.geek.modules.question.vo.QuestionListVo;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 填充点赞信息
     * @param uid
     * @param questionListVo
     */
    private void getInfoPraise(String uid, QuestionListVo questionListVo) {
        //获取当前登录用户id
        String userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<UserPraiseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPraiseRecord::getResourceType,uid);
        queryWrapper.eq(UserPraiseRecord::getStatus,1);
        //获取点赞数量
        int count = userPraiseRecordService.count(queryWrapper);
        queryWrapper.eq(UserPraiseRecord::getUserUid,userId);
        UserPraiseRecord userPraiseRecord = userPraiseRecordService.getOne(queryWrapper);
        //设置问答点赞的数量
        PraiseInfo praiseInfo = new PraiseInfo();
        praiseInfo.setPraiseCount(count);
        if (Objects.isNull(userPraiseRecord)){
            praiseInfo.setPraise(false);
        }else {
            praiseInfo.setPraise(true);
        }
        questionListVo.setPraiseInfo(praiseInfo);
    }

    /**
     * 填充user信息
     * @param userUid
     * @param questionListVo
     */
    private void getInfoUser(String userUid, QuestionListVo questionListVo) {
        User user = userService.getById(userUid);
        BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
        questionListVo.setUser(blogInfoUser);
    }

    /**
     * 填充问答标签
     * @param uid
     * @param questionListVo
     */
    private void getQuestionTag(String uid, QuestionListVo questionListVo) {
        LambdaQueryWrapper<QuestionTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestionTag::getQuestionoUid,uid);
        queryWrapper.eq(QuestionTag::getStatus,1);
        List<QuestionTag> list = questionTagService.list(queryWrapper);
        questionListVo.setQuestionTagList(list);
    }
}
