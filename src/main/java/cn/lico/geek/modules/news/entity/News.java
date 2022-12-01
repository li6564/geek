package cn.lico.geek.modules.news.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (News)表实体类
 *
 * @author makejava
 * @since 2022-11-30 19:37:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_news")
public class News{
    //资讯uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //资讯标题
    private String title;
    //资讯简介
    private String summary;
    //资讯内容
    private String content;
    //图片链接
    private String photoUrl;
    //状态
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //浏览量
    private Integer clickCount;

    private Integer oid;

    private Integer isPublish;

}

