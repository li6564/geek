package cn.lico.geek.modules.moment.vo;

import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.moment.entity.UserMomentTopic;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/20 18:11
 */
@Data
public class UserMomentVo {
    private String userUid;

    private String auditStatus;

    private Date auditTime;

    private String auditName;

    private String content;

    private Integer isPublish;

    //private String topicUids;
    //引入的链接
    private String url;

    //链接说明
    private String urlInfo;

    private Integer clickCount;

    private Integer commentCount;

    private Integer openComment;

    private List<String> photoList;

    private BlogInfoUser user;

    private List<UserMomentTopic> userMomentTopicList;

    private PraiseInfo praiseInfo;

    private String uid;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
