package cn.lico.geek.modules.blog.service.Impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.entity.Comment;
import cn.lico.geek.modules.blog.form.CommentDeleteForm;
import cn.lico.geek.modules.blog.form.CommentListForm;
import cn.lico.geek.modules.blog.mapper.CommentMapper;
import cn.lico.geek.modules.blog.service.CommentService;
import cn.lico.geek.modules.blog.vo.CommentChildVo;
import cn.lico.geek.modules.blog.vo.CommentUserVo;
import cn.lico.geek.modules.blog.vo.CommentVo;
import cn.lico.geek.modules.queue.service.IMessageQueueService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author：linan
 * @Date：2022/11/16 16:09
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;

    @Autowired
    private IMessageQueueService messageQueueService;

    /**
     * 获取评论列表
     * @param commentListForm
     * @return
     */
    @Override
    public ResponseResult getList(CommentListForm commentListForm) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //根据blogid获取评论集合
        queryWrapper.eq(Objects.nonNull(commentListForm.getBlogUid())&&commentListForm.getBlogUid().length()>0,Comment::getBlogUid,commentListForm.getBlogUid());
        //评论来源
        queryWrapper.eq(Comment::getSource,commentListForm.getSource());
        queryWrapper.eq(Comment::getStatus,1);
        //先获取根评论
        queryWrapper.isNull(Comment::getToUid);
        //进行分页查询
        Page<Comment> page = new Page<>(commentListForm.getCurrentPage(),commentListForm.getPageSize());
        page(page,queryWrapper);

        List<CommentVo> commentVos = toCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVos) {
            commentVo.setReplyList(getChildren(commentVo.getUid()));
        }
        //封装分页对象
        PageDTO<CommentVo> pageDTO = new PageDTO<>();
        pageDTO.setRecords(commentVos);
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setSize((int)page.getSize());
        return new ResponseResult(pageDTO);
    }
    /**
     * 发布评论
     * @param blogUid
     * @param content
     * @param source
     * @param userUid
     * @return
     */
    @Override
    public ResponseResult add(String blogUid, String content, String source, String userUid, String toUid, String toUserUid) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setSource(source);
        comment.setBlogUid(blogUid);
        comment.setUserUid(userUid);
        //添加被评论者id以及被评论的id
        if (Objects.nonNull(toUid)&&toUid.length() > 0&&Objects.nonNull(toUserUid)&&toUserUid.length() > 0){
            comment.setToUid(toUid);
            comment.setToUserUid(toUserUid);
            comment.setFirstCommentUid(toUid);
        }
        save(comment);

        Comment comment1 = getById(comment.getUid());
        CommentChildVo commentChildVo = BeanCopyUtils.copyBean(comment1, CommentChildVo.class);
        User user = userService.getById(userUid);
        CommentUserVo commentUserVo = BeanCopyUtils.copyBean(user, CommentUserVo.class);
        commentChildVo.setUser(commentUserVo);

        if ("QUESTION_INFO".equals(source)){
            DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
            dataItemChangeMessage.setOperatorId(userUid);
            dataItemChangeMessage.setItemId(blogUid);
            dataItemChangeMessage.setItemType(DataItemType.QUESTION_REPLY);
            dataItemChangeMessage.setChangeType(DataItemChangeType.ADD);
            messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
        }
        return new ResponseResult(commentChildVo);
    }

    /**
     * 删除评论及其子评论
     * @param deleteForm
     * @return
     */
    @Override
    public ResponseResult delete(CommentDeleteForm deleteForm) {
        //先获取要删除的评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getUid,deleteForm.getUid());
        queryWrapper.eq(Comment::getUserUid,deleteForm.getUserUid());
        Comment comment = getOne(queryWrapper);

        List<String> list = new ArrayList<>();

        //获取子评论
        List<String> ChildCommentIds = getChildCommentIds(comment.getUid(),list);
        ChildCommentIds.add(comment.getUid());
        removeByIds(ChildCommentIds);

        return new ResponseResult("删除成功", AppHttpCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 获取子评论的uid集合
     * @param uid
     * @return
     */
    private List<String> getChildCommentIds(String uid,List list) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getToUid,uid);
        List<Comment> comments = list(queryWrapper);

        for (Comment comment : comments) {
            list.add(comment.getUid());
        }

        return list;
    }


    /**
     * 获取子评论
     * @param uid
     * @return
     */
    private List<CommentVo> getChildren(String uid) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getToUid,uid);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    /**
     * 为文章评论添加评论者信息
     * @param records
     * @return
     */
    private List<CommentVo> toCommentVoList(List<Comment> records) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(records, CommentVo.class);
        for (CommentVo commentVo : commentVos) {
            User user = userService.getById(commentVo.getUserUid());
            CommentUserVo commentUserVo = BeanCopyUtils.copyBean(user, CommentUserVo.class);
            commentVo.setUser(commentUserVo);
        }
        return commentVos;
    }
}
