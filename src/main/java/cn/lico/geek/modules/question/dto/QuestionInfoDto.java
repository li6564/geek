package cn.lico.geek.modules.question.dto;

import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.question.entity.QuestionTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/23 17:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionInfoDto {
    private String oid;

    private String title;

    private String summary;

    private String content;

    private Integer clickCount;

    private Integer replyCount;

    private Integer collectCount;

    private Integer isPublish;

    private Integer sort;

    private Integer openComment;

    private String userUid;

    private Integer questionStatus;

    private Integer auditStatus;

    private List<QuestionTag> questionTag;

    private BlogInfoUser user;

    private String uid;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
