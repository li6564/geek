package cn.lico.geek.modules.blog.form;

import lombok.Data;

/**
 * @Author：linan
 * @Date：2022/11/16 20:55
 */
@Data
public class CommentForm {
    private String blogUid;
    private String content;
    private String source;
    private String userUid;
    private String toUid;
    private String toUserUid;
}
