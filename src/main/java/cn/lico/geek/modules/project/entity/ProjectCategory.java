package cn.lico.geek.modules.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Date;
import java.io.Serializable;

/**
 * (ProjectCategory)表实体类
 *
 * @author makejava
 * @since 2023-02-19 14:19:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_project_category")
public class ProjectCategory{
    @TableId(value = "uid",type = IdType.AUTO)
    private Integer uid;

    private String parentUid;
    
    private String name;
    
    private Integer status;
    
    private Integer sort;
    
    private String icon;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}

