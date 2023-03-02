package cn.lico.geek.modules.report.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 内容举报信息(Report)表实体类
 *
 * @author makejava
 * @since 2023-03-01 19:27:22
 */
@Data
@TableName(value = "t_report")
public class Report{
    //主键
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //分类（博客/问答/评论）
    private String reportType;
    //被举报人uid
    private String reportUserUid;
    //uid
    private String reportContentUid;
    //举报人uid
    private String userUid;
    //进展状态: 0 未处理   1: 已查看  2：已处理
    private Integer progress;
    //状态  （0 删除  1正常）
    private Integer status;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //违规类型
    private String violationType;
    //举报内容
    private String content;
    //举报标题、内容
    private String reportContent;

}

