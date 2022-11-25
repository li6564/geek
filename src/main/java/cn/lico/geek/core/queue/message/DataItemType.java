package cn.lico.geek.core.queue.message;
import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.entity.Comment;
import cn.lico.geek.modules.moment.entity.UserMoment;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserStatistics;
import lombok.Getter;

/**
 * @author meteor
 */
@Getter
public enum DataItemType {

    /**
     * 用户
     */
    USER(User.class),

    /**
     * 用户统计信息
     */
    USER_STATISTICS(UserStatistics.class),

    /**
     * 博客
     */
    BLOG(Blog.class),

    /**
     * 博客浏览量
     */
    BLOG_VIEW_NUM(Blog.class),

    /**
     * 博客评论
     */
    BLOG_COMMENT(Comment.class),

    /**
     * 项目
     */
    //PROJECT(Project.class),

    /**
     * 项目评论
     */
    //PROJECT_COMMENT(ProjectComment.class),

    /**
     * 话题
     */
    //TOPIC(Project.class),

    /**
     * 动态
     */
    POST(UserMoment.class),

    /**
     * 动态评论
     */
    //POST_COMMENT(PostComment.class),

    /**
     * 问答
     */
    QUESTION(Question.class),

    /**
     * 问题回答
     */
    QUESTION_REPLY(Comment.class);

    /**
     * 回答被评论
     */
    //QUESTION_REPLY_COMMENT(QuestionReplyComment.class),


    /**
     * 问题回答 被采纳
     */
    //QUESTION_REPLY_ACCEPT(QuestionReply.class),

    /**
     * 用户关注
     */
    //USER_FOLLOW(UserFollow .class),

    /**
     * 博客点赞
     */
    //BLOG_PRAISE(BlogPraise.class),

    /**
     * 项目点赞
     */
    //PROJECT_PRAISE(ProjectPraise.class),

    /**
     * 动态点赞
     */
    //POST_PRAISE(PostPraise.class),

    /**
     * 问题点赞
     */
    //QUESTION_PRAISE(QuestionPraise.class);

    private Class<?> classOfItem;

    DataItemType(Class<?> classOfItem) {
        this.classOfItem = classOfItem;
    }
}
