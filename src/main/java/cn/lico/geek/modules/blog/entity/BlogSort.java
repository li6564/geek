package cn.lico.geek.modules.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/11/11 15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_blog_sort")
public class BlogSort {
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //分类内容
    private String sortName;
    //分类简介
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //状态
    private Integer status;

    //排序字段，越大越靠前
    private Integer sort;

    //点击数
    private Integer clickCount;

    //分类icon图标
    private String icon;
}
