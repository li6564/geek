package cn.lico.geek.modules.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2022-11-07 18:14:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User{
    //用户id
    private String userId;
    //用户名
    private String userName;
    //密码
    private String password;
    //昵称
    private String nickName;
    //性别（1:男2:女）
    private String gender;
    //自我介绍
    private String summary;
    //头像链接
    private String avatar;
    //邮箱
    private String email;
    //生日
    private Date birthday;
    //手机号
    private String mobile;
    //qq号
    private String qqNumber;
    //微信号
    private String weChat;
    //账号状态默认值为0
    private Integer status;
    
    private Date createTime;
    
    private Date updateTime;

}

