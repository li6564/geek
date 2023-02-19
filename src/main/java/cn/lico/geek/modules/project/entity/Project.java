package cn.lico.geek.modules.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Project)表实体类
 *
 * @author makejava
 * @since 2023-02-19 15:18:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_project")
public class Project{
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;

    private Integer oid;
    //项目名称
    private String name;
    //项目简介
    private String summary;
    //项目描述
    private String content;
    
    private Integer clickcount;
    
    private Integer collectcount;
    //发布者id
    private String useruid;
    
    private Integer projectCategoryId;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}

