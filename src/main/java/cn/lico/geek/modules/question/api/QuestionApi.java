package cn.lico.geek.modules.question.api;

/**
 * @Author：linan
 * @Date：2022/11/21 20:33
 */

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.question.form.QuestionListForm;
import cn.lico.geek.modules.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
public class QuestionApi {
    @Autowired
    private QuestionService questionService;

    /**
     * 获取问答列表
     * @param questionListForm
     * @return
     */
    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody QuestionListForm questionListForm){
        return questionService.getList(questionListForm);
    }
}
