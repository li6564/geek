package cn.lico.geek.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 用户点赞表(UserPraiseRecord)表实体类
 *
 * @author makejava
 * @since 2022-11-14 14:03:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_user_praise_record")
public class UserPraiseRecord{
    //点赞记录uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //资源类型
    private String resourceType;
    //资源uid
    private String resourceUid;
    //用户uid
    private String userUid;
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
    //点赞类型(目前默认为1 点赞 0  点踩 )
    private Integer praiseType;

}

