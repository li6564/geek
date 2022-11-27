package cn.lico.geek.modules.notice.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.entity.Comment;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.modules.blog.service.CommentService;
import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.blog.vo.NewBlogUserVo;
import cn.lico.geek.modules.blog.vo.NewBlogVo;
import cn.lico.geek.modules.moment.entity.UserMoment;
import cn.lico.geek.modules.moment.service.UserMomentService;
import cn.lico.geek.modules.moment.vo.UserMomentVo;
import cn.lico.geek.modules.notice.dto.UserReceiveNoticeCountDto;
import cn.lico.geek.modules.notice.dto.UserReceiveNoticeListDto;
import cn.lico.geek.modules.notice.entity.Notice;
import cn.lico.geek.modules.notice.form.NoticeListForm;
import cn.lico.geek.modules.notice.mapper.NoticeMapper;
import cn.lico.geek.modules.notice.service.NoticeService;
import cn.lico.geek.modules.notice.vo.NoticeCommentVo;
import cn.lico.geek.modules.notice.vo.NoticeUserWatchVo;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.question.service.QuestionService;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/25 14:15
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserMomentService userMomentService;

    @Autowired
    private UserWatchService userWatchService;

    @Autowired
    private UserPraiseRecordService userPraiseRecordService;

    /**
     * 获取当前登录用户通知消息数量
     * @return
     */
    @Override
    public ResponseResult getUserReceiveNoticeCount() {
        UserReceiveNoticeCountDto receiveNoticeCountDto = new UserReceiveNoticeCountDto();
        //如果用户登录则查询数据库中用户的消息
        if (SecurityUtils.isLogin()){
            //获取当前用户id
            String userId = SecurityUtils.getUserId();
            List<Integer> countList = new ArrayList<>();
            for(int i = 1;i <= 4;i++){
                LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Notice::getNoticeStatus,0);
                queryWrapper.eq(Notice::getStatus,1);
                queryWrapper.eq(Notice::getNoticeType,i);
                queryWrapper.eq(Notice::getUserUid,userId);
                int count = count(queryWrapper);
                countList.add(count);
            }
            receiveNoticeCountDto.setUserReceiveCommentCount(countList.get(0));
            receiveNoticeCountDto.setUserReceiveWatchCount(countList.get(1));
            receiveNoticeCountDto.setUserReceivePraiseCount(countList.get(2));
            receiveNoticeCountDto.setUserReceiveSystemCount(countList.get(3));
        }else{
            receiveNoticeCountDto.setUserReceiveSystemCount(0).setUserReceivePraiseCount(0).setUserReceiveWatchCount(0).setUserReceiveCommentCount(0).setUserReceiveMessageCount(0).setUserReceiveCollectCount(0);
        }
        //如果没有登录则返回消息数量为0
        return new ResponseResult(receiveNoticeCountDto);
    }

    /**
     * 获取消息列表
     * @param noticeListForm
     * @return
     */
    @Override
    public ResponseResult getList(NoticeListForm noticeListForm) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();
        //根据条件获取消息列表
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getStatus,1);
        queryWrapper.eq(Notice::getUserUid,userId);
        queryWrapper.eq(Notice::getNoticeType,noticeListForm.getNoticeType());
        queryWrapper.orderByDesc(Notice::getCreateTime);
        //根据分页查询
        Page<Notice> page = new Page<>(noticeListForm.getCurrentPage(),noticeListForm.getPageSize());
        page(page,queryWrapper);
        //转化为需要返回的形式
        List<UserReceiveNoticeListDto> userReceiveNoticeListDtos = BeanCopyUtils.copyBeanList(page.getRecords(), UserReceiveNoticeListDto.class);

        for (UserReceiveNoticeListDto userReceiveNoticeListDto : userReceiveNoticeListDtos) {
            if (userReceiveNoticeListDto.getNoticeType() == 1){
                //获取业务类型
                Integer businessType = userReceiveNoticeListDto.getBusinessType();
                NoticeCommentVo noticeCommentVo = new NoticeCommentVo();
                //根据评论uid 获取评论信息
                Comment comment = commentService.getById(userReceiveNoticeListDto.getBusinessUid());
                //根据评论uid获取评论者信息
                User user = userService.getById(comment.getUserUid());
                BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
                noticeCommentVo.setUser(blogInfoUser);
                noticeCommentVo.setContent(comment.getContent());
                noticeCommentVo.setUid(comment.getUid());
                if (businessType == 1||businessType == 3){
                    //根据博客uid获取博客信息
                    LambdaQueryWrapper<Blog> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(Blog::getUid,comment.getBlogUid());
                    queryWrapper1.eq(Blog::getStatus,1);
                    Blog blog = blogService.getOne(queryWrapper1);
                    noticeCommentVo.setBlog(blog);
                    userReceiveNoticeListDto.setComment(noticeCommentVo);
                }else if (businessType == 2 || businessType == 4){
                    //根据问答uid获取问答信息
                    LambdaQueryWrapper<Question> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(Question::getStatus,1);
                    queryWrapper1.eq(Question::getUid,comment.getBlogUid());
                    Question question = questionService.getOne(queryWrapper1);
                    noticeCommentVo.setQuestion(question);
                    userReceiveNoticeListDto.setComment(noticeCommentVo);
                }else if (businessType == 12 || businessType == 13){
                    //根据问答uid获取动态信息
                    LambdaQueryWrapper<UserMoment> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(UserMoment::getStatus,1);
                    queryWrapper1.eq(UserMoment::getUid,comment.getBlogUid());
                    UserMoment moment = userMomentService.getOne(queryWrapper1);
                    noticeCommentVo.setBlogUid(moment.getUid());
                    userReceiveNoticeListDto.setComment(noticeCommentVo);
                }
            }else if(userReceiveNoticeListDto.getNoticeType() == 2){
                //根据获取BusinessUid获取关注信息
                UserWatch userWatch = userWatchService.getById(userReceiveNoticeListDto.getBusinessUid());
                //根据关注者uid获取关注者信息
                User user = userService.getById(userWatch.getUserUid());
                BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
                NoticeUserWatchVo noticeUserWatchVo = new NoticeUserWatchVo(blogInfoUser);
                userReceiveNoticeListDto.setUserWatch(noticeUserWatchVo);
            }else if (userReceiveNoticeListDto.getNoticeType() == 3){
                //获取业务类型
                Integer businessType = userReceiveNoticeListDto.getBusinessType();
                //根据点赞uid 获取点赞信息
                UserPraiseRecord userPraiseRecord = userPraiseRecordService.getById(userReceiveNoticeListDto.getBusinessUid());
                //根据评论uid获取点赞者信息
                User user = userService.getById(userPraiseRecord.getUserUid());
                BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
                userReceiveNoticeListDto.setFormUser(blogInfoUser);
                if (businessType == 6){
                    LambdaQueryWrapper<Blog> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(Blog::getUid,userPraiseRecord.getResourceUid());
                    queryWrapper1.eq(Blog::getStatus,1);
                    Blog blog = blogService.getOne(queryWrapper1);
                    NewBlogVo newBlogVo = BeanCopyUtils.copyBean(blog, NewBlogVo.class);
                    userReceiveNoticeListDto.setBlog(newBlogVo);
                }else if (businessType == 7){
                    LambdaQueryWrapper<Question> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(Question::getUid,userPraiseRecord.getResourceUid());
                    queryWrapper1.eq(Question::getStatus,1);
                    Question question = questionService.getOne(queryWrapper1);
                    userReceiveNoticeListDto.setQuestion(question);
                }else if(businessType == 14){
                    LambdaQueryWrapper<UserMoment> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(UserMoment::getUid,userPraiseRecord.getResourceUid());
                    queryWrapper1.eq(UserMoment::getStatus,1);
                    UserMoment userMoment = userMomentService.getOne(queryWrapper1);
                    UserMomentVo userMomentVo = BeanCopyUtils.copyBean(userMoment, UserMomentVo.class);
                    userReceiveNoticeListDto.setUserMoment(userMomentVo);
                }
            }else if(userReceiveNoticeListDto.getNoticeType() == 4){
                //获取业务类型
                Integer businessType = userReceiveNoticeListDto.getBusinessType();
                if (businessType == 8){
                    Blog blog = blogService.getById(userReceiveNoticeListDto.getBusinessUid());
                    NewBlogVo newBlogVo = BeanCopyUtils.copyBean(blog, NewBlogVo.class);
                    User user = userService.getById(blog.getUserUid());
                    NewBlogUserVo newBlogUserVo = BeanCopyUtils.copyBean(user, NewBlogUserVo.class);
                    newBlogVo.setUser(newBlogUserVo);
                    userReceiveNoticeListDto.setBlog(newBlogVo);
                }else if(businessType == 22){
                    UserMoment userMoment = userMomentService.getById(userReceiveNoticeListDto.getBusinessUid());
                    UserMomentVo userMomentVo = BeanCopyUtils.copyBean(userMoment, UserMomentVo.class);
                    User user = userService.getById(userMoment.getUserUid());
                    BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
                    userMomentVo.setUser(blogInfoUser);
                    userReceiveNoticeListDto.setUserMoment(userMomentVo);
                }
            }
        }
        PageDTO<UserReceiveNoticeListDto> pageDTO = new PageDTO<>();
        pageDTO.setRecords(userReceiveNoticeListDtos);
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setSize((int)page.getSize());
        pageDTO.setTotal((int)page.getTotal());
        return new ResponseResult(pageDTO);
    }
}
