package cn.lico.geek.modules.user.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.modules.question.service.QuestionService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.enums.UserErrorCode;
import cn.lico.geek.modules.user.exception.UserServiceException;
import cn.lico.geek.modules.user.form.PageForm;
import cn.lico.geek.modules.user.form.UserBlogForm;
import cn.lico.geek.modules.user.form.UserWatchListForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：linan
 * @Date：2022/12/2 14:50
 */
@RestController
@RequestMapping("/user")
public class UserApi {
    @Autowired
    private UserStatisticsService userStatisticsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserWatchService userWatchService;

    /**
     * 获得社区精英列表
     * @param pageForm
     * @return
     */
    @PostMapping("/getUserTopN")
    public ResponseResult getUserTopN(@RequestBody PageForm pageForm){
        return userStatisticsService.getUserTopN(pageForm);
    }

    /**
     * 根据用户id获取用户信息
     * @param userUid
     * @return
     */
    @GetMapping("/getUserByUid")
    public ResponseResult getUserByUid(@RequestParam String userUid){
        try {
            return userService.getUserByUid(userUid);
        } catch (UserServiceException e) {
            e.printStackTrace();
            return new ResponseResult(UserErrorCode.USER_NOT_FOUND.msg(), AppHttpCodeEnum.ERROR.getMsg());
        }
    }

    /**
     * 根据uid获取用户统计信息
     * @param userUid
     * @return
     */
    @GetMapping("/getUserCenterByUid")
    public ResponseResult getUserCenterByUid(@RequestParam String userUid){
        return userService.getUserCenterByUid(userUid);
    }

    /**
     * 获取指定用户博客列表
     * @param userBlogForm
     * @return
     */
    @PostMapping("/getBlogListByUser")
    public ResponseResult getBlogListByUser(@RequestBody UserBlogForm userBlogForm){
        return blogService.getBlogListByUser(userBlogForm);
    }

    /**
     * 获取指定用户问答列表
     * @param userBlogForm
     * @return
     */
    @PostMapping("/getQuestionListByUser")
    public ResponseResult getQuestionListByUser(@RequestBody UserBlogForm userBlogForm){
        return questionService.getQuestionListByUser(userBlogForm);
    }

    /**
     * 获取指定用户的粉丝或关注人
     * @param userWatchListForm
     * @return
     */
    @PostMapping("/getUserWatchList")
    public ResponseResult getUserWatchList(@RequestBody UserWatchListForm userWatchListForm){
        return userWatchService.getUserWatchList(userWatchListForm);
    }

    /**
     * 编辑用户资料
     * @param user
     * @return
     */
    @PostMapping("/editUser")
    public ResponseResult editUser(@RequestBody User user){
        return userService.editUser(user);
    }
}
