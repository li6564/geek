package cn.lico.geek.modules.blog.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.form.BlogPraiseCountForm;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：linan
 * @Date：2022/11/18 15:43
 */
@RestController
@RequestMapping("/blogPraise")
public class BlogPraiseApi {
    @Autowired
    private UserPraiseRecordService userPraiseRecordService;

    /**
     * 获取博客的点赞信息
     * @param blogPraiseCountForm
     * @return
     */
    @PostMapping("/getPraiseCount")
    public ResponseResult getPraiseCount(@RequestBody BlogPraiseCountForm blogPraiseCountForm){
        return userPraiseRecordService.getPraiseCount(blogPraiseCountForm);
    }

    /**
     * 博客点赞
     * @param blogPraiseCountForm
     * @return
     */
    @PostMapping("/praise")
    public ResponseResult praise(@RequestBody BlogPraiseCountForm blogPraiseCountForm){
        return userPraiseRecordService.praise(blogPraiseCountForm);
    }

    @PostMapping("/cancelPraise")
    public ResponseResult cancelPraise(@RequestBody BlogPraiseCountForm blogPraiseCountForm){
        return userPraiseRecordService.cancelPraise(blogPraiseCountForm);
    }
}
