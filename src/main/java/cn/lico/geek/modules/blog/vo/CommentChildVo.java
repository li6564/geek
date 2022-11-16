package cn.lico.geek.modules.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/11/16 20:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentChildVo {
    private String userUid;

    private String toUid;

    private String firstCommentUid;

    private String toUserUid;

    private String content;

    private String blogUid;

    private String source;

    private String auditStatus;

    private String auditTime;

    private String auditName;

    private CommentUserVo user;

    private String uid;

    private String status;

    private Date createTime;

    private Date updateTime;
}
