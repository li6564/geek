package cn.lico.geek.modules.user.enums;

import cn.lico.geek.core.IErrorCode;

/**
 * @author linan
 * @date 2022/11/8 10:45
 */
public enum UserErrorCode implements IErrorCode {
    USER_NOT_FOUND(8002,"当前用户已经不存在");


    private long code;

    private String msg;

    UserErrorCode(long code, String msg) {
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
