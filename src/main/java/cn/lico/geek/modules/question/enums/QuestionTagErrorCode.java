package cn.lico.geek.modules.question.enums;

import cn.lico.geek.core.IErrorCode;

/**
 * @author linan
 * @date 2022/11/20 10:42
 */
public enum QuestionTagErrorCode implements IErrorCode {
    QUESTION_TAG_ERROR_CODE(8003,"该标签不存在")
    ;

    private long code;

    private String msg;

    QuestionTagErrorCode(long code, String msg) {
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
