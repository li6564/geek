package cn.lico.geek.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2022-11-12 12:40:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user")
public class User{
    //唯一uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //用户名
    private String userName;
    //密码
    private String passWord;
    //性别(1:男2:女)
    private Integer gender;
    //个人头像
    private String photoUrl;
    //邮箱
    private String email;
    //出生年月日
    private Date birthday;
    //手机
    private String mobile;
    //邮箱验证码
    private String validCode;
    //自我简介最多150字
    private String summary;
    //登录次数
    private Integer loginCount;
    //最后登录时间
    private Date lastLoginTime;
    //最后登录IP
    private String lastLoginIp;
    //状态
    private Integer status;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //昵称
    private String nickName;
    //资料来源
    private String source;
    //平台uuid
    private String uuid;
    //QQ号
    private String qqNumber;
    //微信号
    private String weChat;
    //职业
    private String occupation;
    //评论状态 1:正常 0:禁言
    private Integer commentStatus;
    //ip来源
    private String ipSource;
    //浏览器
    private String browser;
    //操作系统
    private String os;
    //是否开启邮件通知 1:开启 0:关闭
    private Integer startEmailNotification;
    //用户标签：0：普通用户，1：管理员，2：博主 等
    private Integer userTag;
    //是否通过加载校验【0 未通过，1 已通过】
    private Integer loadingValid;
    //积分
    private Integer credits;
    //个人中心背景图片
    private String backgroundFileUid;
    //编辑器模式，(0：富文本编辑器CKEditor，1：markdown编辑器Veditor)
    private Integer editorModel;

}

