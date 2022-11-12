package cn.lico.geek.modules.blog.enums;

import lombok.Getter;

/**
 * @author linan
 * @date 2022/11/8 10:42
 */
@Getter
public enum BlogPraiseType {

    /**
     * 博客点赞
     */
    BLOG_PRAISE(1),

    /**
     * 博客评论点赞
     */
    BLOG_COMMENT_PRAISE(2),
    ;
    private Integer praiseType;

    BlogPraiseType(Integer praiseType) {
        this.praiseType = praiseType;
    }
}
