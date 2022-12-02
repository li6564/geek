package cn.lico.geek.modules.user.exception;

import cn.lico.geek.core.IErrorCode;
import cn.lico.geek.core.exception.ServiceException;

/**
 * @author linan
 * @date 2022/11/8 10:48
 */
public class UserServiceException extends ServiceException {
    public UserServiceException(long errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public UserServiceException(IErrorCode errorCode) {
        super(errorCode);
    }
}
