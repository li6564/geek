package cn.lico.geek.modules.moment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户话题表(UserMomentTopic)表实体类
 *
 * @author makejava
 * @since 2022-11-20 16:22:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_user_moment_topic")
public class UserMomentTopic{
    //唯一uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //用户uid
    private String userUid;
    //管理员uid
    private String adminUid;
    //内容
    private String content;
    //是否发布：0：否，1：是
    private Integer isPublish;
    //文件uid
    private String fileUid;
    //点击数
    private Integer clickCount;
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
    //排序字段
    private Integer sort;

}

