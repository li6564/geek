package cn.lico.geek.modules.moment.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/11/20 16:50
 */
@Data
public class UserMomentTopicVo {
    private String content;

    private Integer isPublish;

    private Integer clickCount;

    private Integer sort;

    private String uid;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
