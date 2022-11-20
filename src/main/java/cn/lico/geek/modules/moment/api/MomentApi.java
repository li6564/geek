package cn.lico.geek.modules.moment.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.moment.form.UserMomentListForm;
import cn.lico.geek.modules.moment.form.UserMomentTopicPage;
import cn.lico.geek.modules.moment.service.UserMomentService;
import cn.lico.geek.modules.moment.service.UserMomentTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：linan
 * @Date：2022/11/20 16:24
 */
@RestController
@RequestMapping("/moment")
public class MomentApi {
    @Autowired
    private UserMomentTopicService userMomentTopicService;

    @Autowired
    private UserMomentService userMomentService;

    /**
     * 获取动态主题列表
     * @param userMomentTopicPage
     * @return
     */
    @PostMapping("/getUserMomentTopicList")
    public ResponseResult getUserMomentTopicList(@RequestBody UserMomentTopicPage userMomentTopicPage){
        return userMomentTopicService.getUserMomentTopicList(userMomentTopicPage);

    }

    /**
     * 获取动态内容
     * @param userMomentListForm
     * @return
     */
    @PostMapping("/getUserMomentList")
    public ResponseResult getUserMomentList(@RequestBody UserMomentListForm userMomentListForm){
        return userMomentService.getUserMomentList(userMomentListForm);
    }
}
