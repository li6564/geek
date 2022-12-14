package cn.lico.geek.modules.question.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.question.entity.QuestionTag;
import cn.lico.geek.modules.question.form.PageForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/21 20:29
 */
public interface QuestionTagService extends IService<QuestionTag> {
    ResponseResult getTagList(PageForm tagListGetForm);
}
