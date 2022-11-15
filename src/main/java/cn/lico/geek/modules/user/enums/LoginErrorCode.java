package cn.lico.geek.modules.user.enums;

import cn.lico.geek.core.IErrorCode;

/**
 * @author linan
 * @date 2022/11/8 10:45
 */
public enum LoginErrorCode implements IErrorCode {

    USER_NAME_NOT_FOUND(8004, "用户名不存在");


    private long code;

    private String msg;

    LoginErrorCode(long code, String msg) {
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
