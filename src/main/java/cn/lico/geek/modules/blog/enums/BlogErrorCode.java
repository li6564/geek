package cn.lico.geek.modules.blog.enums;

import cn.lico.geek.core.IErrorCode;

/**
 * @author linan
 * @date 2022/11/8 10:45
 */
public enum BlogErrorCode implements IErrorCode {
    USERID_AUTH_ERROR(8001,"不是当前博客的拥有者"),
    BLOG_NOT_FOUND(8002,"当前博客已经不存在"),
    NO_VIEW_PRIVILEGES(8003,"没有查看权限"),
    USER_BLOG_NOT_FOUND(8004, "用户未发布过此博客");


    private long code;

    private String msg;

    BlogErrorCode(long code, String msg) {
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
