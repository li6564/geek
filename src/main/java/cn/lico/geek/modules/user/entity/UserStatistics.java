package cn.lico.geek.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (UserStatistics)表实体类
 *
 * @author makejava
 * @since 2022-11-13 14:37:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_user_statistics")
public class UserStatistics{
    //用户ID
    @TableId
    private String uid;
    //关注数量
    private Integer followedNum;
    //粉丝数量
    private Integer fansNum;
    //博客数量
    private Integer blogNum;
    //博客浏览量
    private Integer blogViewNum;
    //项目数量
    private Integer projectNum;
    //动态数量
    private Integer postNum;
    //问答数量
    private Integer questionNum;
    //问题回复被采纳 次数
    private Integer questionReplyNum;
    //问题回答次数
    private Integer replyAcceptedNum;
    //用户被访问的次数
    private Integer visitedNum;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}

