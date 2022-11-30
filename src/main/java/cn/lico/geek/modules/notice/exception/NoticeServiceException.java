package cn.lico.geek.modules.notice.exception;

import cn.lico.geek.core.IErrorCode;
import cn.lico.geek.core.exception.ServiceException;

/**
 * @author linan
 * @date 2022/11/8 10:48
 */
public class NoticeServiceException extends ServiceException {
    public NoticeServiceException(long errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public NoticeServiceException(IErrorCode errorCode) {
        super(errorCode);
    }
}
