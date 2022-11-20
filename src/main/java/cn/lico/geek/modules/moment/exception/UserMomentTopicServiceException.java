package cn.lico.geek.modules.moment.exception;

import cn.lico.geek.core.IErrorCode;
import cn.lico.geek.core.exception.ServiceException;

/**
 * @author linan
 * @date 2022/11/20 10:47
 */
public class UserMomentTopicServiceException extends ServiceException {
    public UserMomentTopicServiceException(long errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public UserMomentTopicServiceException(IErrorCode errorCode) {
        super(errorCode.code() , errorCode.msg());
    }
}
