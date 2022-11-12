package cn.lico.geek.modules.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 博客表(Blog)表实体类
 *
 * @author makejava
 * @since 2022-11-11 14:56:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_blog")
public class Blog{
    //唯一uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //博客标题
    private String title;
    //博客简介
    private String summary;
    //博客内容
    private String content;

    //博客点击数
    private Integer clickCount;
    //博客收藏数
    private Integer collectCount;
    //标题图片路径
    private String photoList;
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
    //是否原创（0:不是 1：是）
    private String isOriginal;
    //作者
    private String author;
    //文章出处
    private String articlesPart;
    //博客分类UID
    private String blogSortUid;
    //推荐等级(0:正常)
    private Integer level;
    //是否发布：0：否，1：是
    private String isPublish;
    //排序字段
    private Integer sort;
    //是否开启评论(0:否 1:是)
    private Integer openComment;
    //类型【0 博客， 1：推广】
    private Integer type;
    //外链【如果是推广，那么将跳转到外链】
    private String outsideLink;
    //唯一oid
    private Integer oid;
    //投稿用户UID
    private String userUid;
    //文章来源【0 后台添加，1 用户投稿】
    private Integer articleSource;
    //审批状态【0：审核未通过，1：审核通过】
    private Integer auditStatus;
    //审批人
    private String auditName;
    //审批时间
    private Date auditTime;
    //审批拒绝原因
    private String rejectReason;
    //是否开启加载校验，(0：不开启，1：开启)
    private Integer openLoadingValid;
    //是否是特权文章（0：普通文章，1 ：特权文章）
    private Integer isVip;
    //是否仅专栏可见：0 否，1 是
    private Integer isOnlySubjectShow;

}

