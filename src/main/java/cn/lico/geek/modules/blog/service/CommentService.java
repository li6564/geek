package cn.lico.geek.modules.blog.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.entity.Comment;
import cn.lico.geek.modules.blog.form.CommentListForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/16 16:09
 */
public interface CommentService extends IService<Comment> {
    ResponseResult getList(CommentListForm commentListForm);

    ResponseResult add(String blogUid, String content, String source, String userUid, String toUid, String toUserUid);
}
