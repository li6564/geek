package cn.lico.geek.modules.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 评论表(Comment)表实体类
 *
 * @author makejava
 * @since 2022-11-16 16:05:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment{
    //唯一uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //用户uid
    private String userUid;
    //回复某条评论的uid
    private String toUid;
    //回复某个人的uid
    private String toUserUid;
    //评论内容
    private String content;
    //博客uid
    private String blogUid;
    //状态
    private Integer status;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等
    private String source;
    //评论类型 1:点赞 0:评论
    private Integer type;
    //一级评论UID
    private String firstCommentUid;
    //审批状态【0：待审批，1：审核未通过，2：审核通过】
    private Integer auditStatus;
    //审批人
    private String auditName;
    //审批拒绝原因
    private String rejectReason;
    //审批时间
    @TableField(fill = FieldFill.INSERT)
    private Date auditTime;

}

