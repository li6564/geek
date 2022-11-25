package cn.lico.geek.modules.question.api;

/**
 * @Author：linan
 * @Date：2022/11/21 20:33
 */

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.question.form.PageForm;
import cn.lico.geek.modules.question.form.QuestionAddForm;
import cn.lico.geek.modules.question.form.QuestionInfoForm;
import cn.lico.geek.modules.question.form.QuestionListForm;
import cn.lico.geek.modules.question.service.QuestionService;
import cn.lico.geek.modules.question.service.QuestionTagService;
import cn.lico.geek.modules.question.service.QuestionTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/question")
public class QuestionApi {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionTagService questionTagService;

    @Autowired
    private QuestionTemplateService questionTemplateService;

    /**
     * 获取问答列表
     * @param questionListForm
     * @return
     */
    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody QuestionListForm questionListForm){
        return questionService.getList(questionListForm);
    }

    /**
     * 获取问答标签列表
     * @param pageForm
     * @return
     */
    @PostMapping("/getTagList")
    public ResponseResult getTagList(@RequestBody PageForm pageForm){
        return questionTagService.getTagList(pageForm);
    }

    /**
     * 获取问答模板
     * @param pageForm
     * @return
     */
    @PostMapping("/getTemplateList")
    public ResponseResult getTemplateList(@RequestBody PageForm pageForm){
        return questionTemplateService.getTemplateList(pageForm);
    }

    /**
     * 发布问答
     * @param questionAddForm
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody QuestionAddForm questionAddForm){
        return questionService.add(questionAddForm);
    }

    /**
     * 根据问答oid获取问答详情
     * @param questionInfoForm
     * @return
     */
    @RequestMapping("/getQuestion")
    public ResponseResult getQuestion(@RequestBody QuestionInfoForm questionInfoForm){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String remoteHost = requestAttributes.getRequest().getRemoteHost();
        return questionService.getQuestion(remoteHost,questionInfoForm);
    }
}
