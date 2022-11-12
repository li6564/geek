package cn.lico.geek.modules.tag.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/11/12 12:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_tag")
public class Tag {
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;

    private String content;

    private Integer status;

    private Integer clickCount;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //排序字段，越大越靠前
    private Integer sort;
}
