package cn.lico.geek.modules.blog.exception;

import cn.lico.geek.core.IErrorCode;
import cn.lico.geek.core.exception.ServiceException;

/**
 * @author linan
 * @date 2022/11/8 10:47
 */
public class BlogCommentServiceException extends ServiceException {
    public BlogCommentServiceException(long errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public BlogCommentServiceException(IErrorCode errorCode) {
        super(errorCode.code() , errorCode.msg());
    }
}
