package cn.lico.geek.modules.blog.exception;

import cn.lico.geek.core.IErrorCode;
import cn.lico.geek.core.exception.ServiceException;

/**
 * @author linan
 * @date 2022/11/8 10:48
 */
public class BlogServiceException extends ServiceException {
    public BlogServiceException(long errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public BlogServiceException(IErrorCode errorCode) {
        super(errorCode);
    }
}
