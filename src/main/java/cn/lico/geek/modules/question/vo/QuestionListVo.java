package cn.lico.geek.modules.question.vo;

import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.moment.vo.PraiseInfo;
import cn.lico.geek.modules.question.entity.QuestionTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.util.Date;
import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/21 20:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionListVo {
    private Integer oid;

    private String title;

    private String summary;

    private String content;

    private String questionTemplateUid;

    private Integer clickCount;

    private Integer replyCount;

    private Integer collectCount;

    private String isPublish;

    private String sort;

    private String openComment;

    private String userUid;

    private Integer questionStatus;

    private Integer questionSource;

    private Integer auditStatus;

    private List<QuestionTag> questionTagList;

    private BlogInfoUser user;

    private PraiseInfo praiseInfo;

    private String uid;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
