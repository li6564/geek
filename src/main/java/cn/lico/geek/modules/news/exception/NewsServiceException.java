package cn.lico.geek.modules.news.exception;

import cn.lico.geek.core.IErrorCode;
import cn.lico.geek.core.exception.ServiceException;

/**
 * @author linan
 * @date 2022/11/8 10:48
 */
public class NewsServiceException extends ServiceException {
    public NewsServiceException(long errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public NewsServiceException(IErrorCode errorCode) {
        super(errorCode);
    }
}
