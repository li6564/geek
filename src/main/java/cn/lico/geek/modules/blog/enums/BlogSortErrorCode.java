package cn.lico.geek.modules.blog.enums;


import cn.lico.geek.core.IErrorCode;

/**
 * @author li
 *
 */
public enum BlogSortErrorCode implements IErrorCode {
    INCORRECT_BLOG_SORT(8005,"博客分类错误")
    ;

    private long code;

    private String msg;

    BlogSortErrorCode(long code, String msg) {
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
