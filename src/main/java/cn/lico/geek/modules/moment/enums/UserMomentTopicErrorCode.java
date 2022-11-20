package cn.lico.geek.modules.moment.enums;

import cn.lico.geek.core.IErrorCode;

/**
 * @author linan
 * @date 2022/11/20 10:42
 */
public enum UserMomentTopicErrorCode implements IErrorCode {
    TOPIC_NOT_FOUND(8003,"该主题不存在")
    ;

    private long code;

    private String msg;

    UserMomentTopicErrorCode(long code, String msg) {
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
