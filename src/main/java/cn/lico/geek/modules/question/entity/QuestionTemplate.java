package cn.lico.geek.modules.question.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Date;
import java.io.Serializable;

/**
 * 问答表(QuestionTemplate)表实体类
 *
 * @author makejava
 * @since 2022-11-23 14:49:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_question_template")
public class QuestionTemplate{
    //唯一uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //模板名称
    private String name;
    //模板简介
    private String summary;
    //模板内容
    private String content;
    //状态
    private Integer status;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否发布：0：否，1：是
    private String isPublish;
    //排序字段
    private Integer sort;

}

