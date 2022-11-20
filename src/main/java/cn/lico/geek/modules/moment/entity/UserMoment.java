package cn.lico.geek.modules.moment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户动态表(UserMoment)表实体类
 *
 * @author linan
 * @since 2022-11-20 17:04:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_user_moment")
public class UserMoment{
    //唯一uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //用户uid
    private String userUid;
    //管理员uid
    private String adminUid;
    //审核人名称
    private String auditName;
    //审核拒绝原因
    private String rejectReason;
    //审核时间
    @TableField(fill = FieldFill.INSERT)
    private Date auditTime;
    //审核状态，0：未审核，1：审核拒绝，2：审核通过
    private Integer auditStatus;
    //动态内容
    private String content;
    //是否发布：0：否，1：是
    private Integer isPublish;
    //URL链接
    private String url;
    //URL链接信息
    private String urlInfo;
    //点击数
    private Integer clickCount;
    //评论数
    private Integer commentCount;
    //备注
    private String remark;
    //状态
    private Integer status;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否开启评论
    private Integer openComment;
    //排序字段
    private Integer sort;

}

