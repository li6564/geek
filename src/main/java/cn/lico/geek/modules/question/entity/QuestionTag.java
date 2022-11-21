package cn.lico.geek.modules.question.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 问答标签表(QuestionTag)表实体类
 *
 * @author makejava
 * @since 2022-11-21 20:26:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_question_tag")
public class QuestionTag{
    //唯一uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //父uid
    private String parentUid;
    //标签名
    private String name;
    //标签简介
    private String summary;
    //状态
    private Integer status;
    //点击数
    private Integer clickCount;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //排序字段，越大越靠前
    private Integer sort;
    //问答uid
    private String questionoUid;

}

