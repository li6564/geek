package cn.lico.geek.modules.question.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Date;
import java.io.Serializable;

/**
 * 问答表(Question)表实体类
 *
 * @author makejava
 * @since 2022-11-21 20:16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_question")
public class Question{
    //唯一uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //唯一oid
    private Integer oid;
    //问答标题
    private String title;
    //问答简介
    private String summary;
    //问答内容
    private String content;
    //问答点击数
    private Integer clickCount;
    //问答收藏数
    private Integer collectCount;
    //回答次数
    private Integer replyCount;
    //标题图片uid
    private String photoUrl;
    //状态
    private Integer status;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //管理员uid
    private String adminUid;
    //用户UID
    private String userUid;
    //是否发布：0：否，1：是
    private String isPublish;
    //问答状态，0:创建，1:进行，2:已采纳
    private Integer questionStatus;
    //排序字段
    private Integer sort;
    //是否开启评论(0:否 1:是)
    private Integer openComment;
    //问答模板UID
    private String questionTemplateUid;
    //问答来源【0 后台添加，1 用户投稿】
    private Integer questionSource;
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

