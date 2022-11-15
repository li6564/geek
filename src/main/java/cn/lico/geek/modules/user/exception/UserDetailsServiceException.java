package cn.lico.geek.modules.user.exception;

import cn.lico.geek.core.IErrorCode;
import cn.lico.geek.core.exception.ServiceException;

/**
 * @author linan
 * @date 2022/11/8 10:48
 */
public class UserDetailsServiceException extends ServiceException {
    public UserDetailsServiceException(long errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public UserDetailsServiceException(IErrorCode errorCode) {
        super(errorCode);
    }
}
