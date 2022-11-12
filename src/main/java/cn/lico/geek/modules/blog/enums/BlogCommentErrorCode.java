package cn.lico.geek.modules.blog.enums;

import cn.lico.geek.core.IErrorCode;

/**
 * @author linan
 * @date 2022/11/8 10:42
 */
public enum BlogCommentErrorCode implements IErrorCode {
    COMMENT_NOT_FOUND(8003,"该评论不存在"),
    NO_DELETE_PRIVILEGES(8004,"没有权限删除评论"),
    PARENT_COMMENT_NOT_FOUND(8006,"父评论不存在"),
    NO_COMMENT_PRIVILEGES(8007,"没有评论权限")
    ;

    private long code;

    private String msg;

    BlogCommentErrorCode(long code, String msg) {
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
