package cn.lico.geek.modules.question.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.question.form.QuestionListForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/21 20:25
 */
public interface QuestionService extends IService<Question> {
    ResponseResult getList(QuestionListForm questionListForm);
}
