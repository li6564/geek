package cn.lico.geek.modules.notice.dto;

import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.blog.vo.NewBlogVo;
import cn.lico.geek.modules.moment.vo.UserMomentVo;
import cn.lico.geek.modules.notice.vo.NoticeCommentVo;
import cn.lico.geek.modules.notice.vo.NoticeUserWatchVo;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.user.entity.UserWatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/11/25 16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReceiveNoticeListDto {
    //唯一uid
    private String uid;
    //用户uid
    private String userUid;
    //管理员uid
    private String adminUid;
    //通知状态：0:已创建，1:已查看
    private Integer noticeStatus;
    //通知类型(评论1，关注2，点赞3，系统4,收藏6，私信5)
    private Integer noticeType;
    //通知内容
    private String content;
    //业务uid
    private String businessUid;
    //业务类型 【博客，问答，评论】（1文章，2问答，3在文章中回复评论，4在问答中回复评论，10在留言板页面恢复了评论
    // 13动态）
    private Integer businessType;
    //状态
    private Integer status;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //是否前台通知 【0 前台  1 后台】
    private Integer isBlack;

    private NewBlogVo blog;

    private UserMomentVo userMoment;

    private BlogInfoUser formUser;

    private Question question;

    private NoticeCommentVo comment;

    private NoticeUserWatchVo userWatch;
}
