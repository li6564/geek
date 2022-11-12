package cn.lico.geek.modules.blog.exception;

import cn.lico.geek.core.IErrorCode;
import cn.lico.geek.core.exception.ServiceException;

/**
 * @author linan
 * @date 2022/11/8 10:47
 */
public class BlogPraiseServiceException extends ServiceException {
    public BlogPraiseServiceException(long errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public BlogPraiseServiceException(IErrorCode errorCode) {
        super(errorCode.code() , errorCode.msg());
    }
}
