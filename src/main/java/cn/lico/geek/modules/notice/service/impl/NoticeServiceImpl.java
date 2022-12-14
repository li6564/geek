package cn.lico.geek.modules.notice.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
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
import cn.lico.geek.modules.notice.enums.NoticeErrorCode;
import cn.lico.geek.modules.notice.exception.NoticeServiceException;
import cn.lico.geek.modules.notice.form.NoticeDeleteForm;
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
import java.util.Objects;

/**
 * @Author???linan
 * @Date???2022/11/25 14:15
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
     * ??????????????????????????????????????????
     * @return
     */
    @Override
    public ResponseResult getUserReceiveNoticeCount() {
        UserReceiveNoticeCountDto receiveNoticeCountDto = new UserReceiveNoticeCountDto();
        //??????????????????????????????????????????????????????
        if (SecurityUtils.isLogin()){
            //??????????????????id
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
            receiveNoticeCountDto.setUserReceiveCollectCount(0);
            receiveNoticeCountDto.setUserReceiveMessageCount(0);
        }else{
            receiveNoticeCountDto.setUserReceiveSystemCount(0).setUserReceivePraiseCount(0).setUserReceiveWatchCount(0).setUserReceiveCommentCount(0).setUserReceiveMessageCount(0).setUserReceiveCollectCount(0);
        }
        //??????????????????????????????????????????0
        return new ResponseResult(receiveNoticeCountDto);
    }

    /**
     * ??????????????????
     * @param noticeListForm
     * @return
     */
    @Override
    public ResponseResult getList(NoticeListForm noticeListForm) {
        //??????????????????id
        String userId = SecurityUtils.getUserId();
        //??????????????????????????????
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getStatus,1);
        queryWrapper.eq(Notice::getUserUid,userId);
        queryWrapper.eq(Notice::getNoticeType,noticeListForm.getNoticeType());
        queryWrapper.orderByDesc(Notice::getCreateTime);
        //??????????????????
        Page<Notice> page = new Page<>(noticeListForm.getCurrentPage(),noticeListForm.getPageSize());
        page(page,queryWrapper);
        //??????????????????????????????
        List<UserReceiveNoticeListDto> userReceiveNoticeListDtos = BeanCopyUtils.copyBeanList(page.getRecords(), UserReceiveNoticeListDto.class);

        for (UserReceiveNoticeListDto userReceiveNoticeListDto : userReceiveNoticeListDtos) {
            if (userReceiveNoticeListDto.getNoticeType() == 1){
                //??????????????????
                Integer businessType = userReceiveNoticeListDto.getBusinessType();
                NoticeCommentVo noticeCommentVo = new NoticeCommentVo();
                //????????????uid ??????????????????
                Comment comment = commentService.getById(userReceiveNoticeListDto.getBusinessUid());
                //????????????uid?????????????????????
                User user = userService.getById(comment.getUserUid());
                BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
                noticeCommentVo.setUser(blogInfoUser);
                noticeCommentVo.setContent(comment.getContent());
                noticeCommentVo.setUid(comment.getUid());
                if (businessType == 1||businessType == 3){
                    //????????????uid??????????????????
                    LambdaQueryWrapper<Blog> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(Blog::getUid,comment.getBlogUid());
                    queryWrapper1.eq(Blog::getStatus,1);
                    Blog blog = blogService.getOne(queryWrapper1);
                    noticeCommentVo.setBlog(blog);
                    userReceiveNoticeListDto.setComment(noticeCommentVo);
                }else if (businessType == 2 || businessType == 4){
                    //????????????uid??????????????????
                    LambdaQueryWrapper<Question> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(Question::getStatus,1);
                    queryWrapper1.eq(Question::getUid,comment.getBlogUid());
                    Question question = questionService.getOne(queryWrapper1);
                    noticeCommentVo.setQuestion(question);
                    userReceiveNoticeListDto.setComment(noticeCommentVo);
                }else if (businessType == 12 || businessType == 13){
                    //????????????uid??????????????????
                    LambdaQueryWrapper<UserMoment> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(UserMoment::getStatus,1);
                    queryWrapper1.eq(UserMoment::getUid,comment.getBlogUid());
                    UserMoment moment = userMomentService.getOne(queryWrapper1);
                    noticeCommentVo.setBlogUid(moment.getUid());
                    userReceiveNoticeListDto.setComment(noticeCommentVo);
                }
                //????????????????????????
                Notice notice = new Notice();
                notice.setUid(userReceiveNoticeListDto.getUid()).setNoticeStatus(1);
                updateById(notice);
            }else if(userReceiveNoticeListDto.getNoticeType() == 2){
                //????????????BusinessUid??????????????????
                UserWatch userWatch = userWatchService.getById(userReceiveNoticeListDto.getBusinessUid());
                //???????????????uid?????????????????????
                User user = userService.getById(userWatch.getUserUid());
                BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
                NoticeUserWatchVo noticeUserWatchVo = new NoticeUserWatchVo(blogInfoUser);
                userReceiveNoticeListDto.setUserWatch(noticeUserWatchVo);
                //????????????????????????
                Notice notice = new Notice();
                notice.setUid(userReceiveNoticeListDto.getUid()).setNoticeStatus(1);
                updateById(notice);
            }else if (userReceiveNoticeListDto.getNoticeType() == 3){
                //??????????????????
                Integer businessType = userReceiveNoticeListDto.getBusinessType();
                //????????????uid ??????????????????
                UserPraiseRecord userPraiseRecord = userPraiseRecordService.getById(userReceiveNoticeListDto.getBusinessUid());
                //????????????uid?????????????????????
                User user = userService.getById(userPraiseRecord.getUserUid());
                BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
                userReceiveNoticeListDto.setFromUser(blogInfoUser);
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
                //????????????????????????
                Notice notice = new Notice();
                notice.setUid(userReceiveNoticeListDto.getUid()).setNoticeStatus(1);
                updateById(notice);
            }else if(userReceiveNoticeListDto.getNoticeType() == 4){
                //??????????????????
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
                //????????????????????????
                Notice notice = new Notice();
                notice.setUid(userReceiveNoticeListDto.getUid()).setNoticeStatus(1);
                updateById(notice);
            }
        }
        PageDTO<UserReceiveNoticeListDto> pageDTO = new PageDTO<>();
        pageDTO.setRecords(userReceiveNoticeListDtos);
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setSize((int)page.getSize());
        pageDTO.setTotal((int)page.getTotal());
        return new ResponseResult(pageDTO);
    }

    /**
     * ??????????????????
     * @param noticeDeleteForm
     * @return
     */
    @Override
    public ResponseResult delete(NoticeDeleteForm noticeDeleteForm) throws NoticeServiceException{
        //??????????????????????????????uid
        String uid = noticeDeleteForm.getUid();
        //????????????
        boolean flag = removeById(uid);
        if (flag){
            return new ResponseResult("???????????????", AppHttpCodeEnum.SUCCESS.getMsg());
        }else {
            throw new NoticeServiceException(NoticeErrorCode.NOTICE_ERROR_CODE);
        }

    }

    /**
     * ??????????????????
     * @param
     * @return
     */
    @Override
    public ResponseResult deleteBatch(List uids) {
        if(Objects.nonNull(uids)&&uids.size()>0){
            removeByIds(uids);
            return new ResponseResult("?????????????????????",AppHttpCodeEnum.SUCCESS.getMsg());
        }else {
            return new ResponseResult("???????????????",AppHttpCodeEnum.SUCCESS.getMsg());
        }
    }
}
