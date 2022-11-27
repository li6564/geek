package cn.lico.geek.modules.notice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.io.Serializable;

/**
 * 通知表(Notice)表实体类
 *
 * @author makejava
 * @since 2022-11-25 14:11:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "t_notice")
public class Notice{
    //唯一uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //用户uid
    private String userUid;
    //管理员uid
    private String adminUid;
    //通知状态：0:已创建，1:已查看
    private Integer noticeStatus;
    //通知类型(评论1，关注2，点赞3，系统4,收藏6，私信5)
    private Integer noticeType;
    //通知内容
    private String content;
    //业务uid
    private String businessUid;
    //业务类型 【博客，问答，评论】（1文章，2问答，3在文章中回复评论，4在问答中回复评论，10在留言板页面恢复了评论
    // 13动态）
    private Integer businessType;
    //状态
    private Integer status;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否前台通知 【0 前台  1 后台】
    private Integer isBlack;

}

