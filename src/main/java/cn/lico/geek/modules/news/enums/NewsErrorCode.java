package cn.lico.geek.modules.news.enums;

import cn.lico.geek.core.IErrorCode;

/**
 * @author linan
 * @date 2022/11/8 10:45
 */
public enum NewsErrorCode implements IErrorCode {

    NEWS_NOT_FOUND(8002,"当前资讯已经不存在");



    private long code;

    private String msg;

    NewsErrorCode(long code, String msg) {
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
