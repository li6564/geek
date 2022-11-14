package cn.lico.geek.modules.webNavbar.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (WebNavbar)表实体类
 *
 * @author makejava
 * @since 2022-11-13 19:45:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_web_navbar")
public class WebNavbar{
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    
    private String name;
    
    private Integer navbarLevel;
    
    private String summary;
    
    private String parentUid;
    
    private String url;
    
    private String icon;
    
    private Integer isShow;
    
    private Integer isJumpExternalUrl;
    
    private Integer sort;
    
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}

