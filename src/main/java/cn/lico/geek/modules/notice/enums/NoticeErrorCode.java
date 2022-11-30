package cn.lico.geek.modules.notice.enums;

import cn.lico.geek.core.IErrorCode;

/**
 * @author linan
 * @date 2022/11/8 10:45
 */
public enum NoticeErrorCode implements IErrorCode {

    NOTICE_ERROR_CODE(8002,"当前消息已经不存在");


    private long code;

    private String msg;

    NoticeErrorCode(long code, String msg) {
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
