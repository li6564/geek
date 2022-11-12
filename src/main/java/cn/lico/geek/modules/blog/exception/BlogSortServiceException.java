package cn.lico.geek.modules.blog.exception;

import cn.lico.geek.core.IErrorCode;
import cn.lico.geek.core.exception.ServiceException;

/**
 * @author linan
 * @date 2022/11/8 10:47
 */
public class BlogSortServiceException extends ServiceException {
    public BlogSortServiceException(long errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public BlogSortServiceException(IErrorCode errorCode) {
        super(errorCode.code() , errorCode.msg());
    }
}
