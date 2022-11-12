package cn.lico.geek.modules.blog.enums;

import cn.lico.geek.core.IErrorCode;

/**
 * @author linan
 * @date 2022/11/8 10:42
 */
public enum BlogPraiseErrorCode implements IErrorCode {
    BLOG_PRAISE_EXIST(8008,"您已经点过赞了"),
    NO_PRAISE_FOUND(8009,"您没有点过赞")
    ;

    private long code;

    private String msg;

    BlogPraiseErrorCode(long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public long code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
