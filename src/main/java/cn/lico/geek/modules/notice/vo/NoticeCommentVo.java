package cn.lico.geek.modules.notice.vo;

import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.blog.vo.BlogPraiseCountVo;
import cn.lico.geek.modules.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/25 16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeCommentVo {
    private String blogUid;

    private BlogInfoUser user;

    private Blog blog;

    private Question question;

    private String content;

    private String uid;
}
