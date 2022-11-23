package cn.lico.geek.modules.question.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.question.entity.QuestionTemplate;
import cn.lico.geek.modules.question.form.PageForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/23 14:52
 */
public interface QuestionTemplateService extends IService<QuestionTemplate> {
    ResponseResult getTemplateList(PageForm pageForm);
}
