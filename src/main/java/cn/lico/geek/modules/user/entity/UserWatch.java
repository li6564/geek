package cn.lico.geek.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 用户关注表(UserWatch)表实体类
 *
 * @author makejava
 * @since 2022-11-13 22:30:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_user_watch")
public class UserWatch{
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //关注人UID
    private String userUid;
    //被关注者UID
    private String toUserUid;
    //是否是管理员：0否，1是
    private Integer isAdmin;
    //状态
    private Integer status;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}

