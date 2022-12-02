package cn.lico.geek.modules.question.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.question.form.QuestionAddForm;
import cn.lico.geek.modules.question.form.QuestionInfoForm;
import cn.lico.geek.modules.question.form.QuestionListForm;
import cn.lico.geek.modules.user.form.UserBlogForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/21 20:25
 */
public interface QuestionService extends IService<Question> {
    ResponseResult getList(QuestionListForm questionListForm);

    ResponseResult add(QuestionAddForm questionAddForm);

    ResponseResult getQuestion(String remoteHost,QuestionInfoForm questionInfoForm);

    ResponseResult getQuestionListByUser(UserBlogForm userBlogForm);
}
