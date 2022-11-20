package cn.lico.geek.modules.blog.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.form.CommentDeleteForm;
import cn.lico.geek.modules.blog.form.CommentForm;
import cn.lico.geek.modules.blog.form.CommentListForm;
import cn.lico.geek.modules.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：linan
 * @Date：2022/11/16 16:02
 */
@RestController
@RequestMapping("/comment")
public class CommentApi {
    @Autowired
    private CommentService commentService;

    /**
     * 获取评论列表
     * @return
     */
    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody CommentListForm commentListForm){
        return commentService.getList(commentListForm);
    }

    /**
     * 发表评论
     * @param commentForm
     *
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody CommentForm commentForm){
        return commentService.add(commentForm.getBlogUid(),commentForm.getContent(),commentForm.getSource(),
                commentForm.getUserUid(),commentForm.getToUid(),commentForm.getToUserUid());
    }

    /**
     * 删除评论
     * @param deleteForm
     * @return
     */
    @PostMapping("/delete")
    public ResponseResult delete(@RequestBody CommentDeleteForm deleteForm){
        return commentService.delete(deleteForm);
    }
}
