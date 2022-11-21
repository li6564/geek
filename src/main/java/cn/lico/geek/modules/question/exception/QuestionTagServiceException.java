package cn.lico.geek.modules.question.exception;

import cn.lico.geek.core.IErrorCode;
import cn.lico.geek.core.exception.ServiceException;

/**
 * @author linan
 * @date 2022/11/20 10:47
 */
public class QuestionTagServiceException extends ServiceException {
    public QuestionTagServiceException(long errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public QuestionTagServiceException(IErrorCode errorCode) {
        super(errorCode.code() , errorCode.msg());
    }
}
