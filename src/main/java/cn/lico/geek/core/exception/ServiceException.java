package cn.lico.geek.core.exception;

import cn.lico.geek.core.IErrorCode;
import lombok.Data;

/**
 * 业务异常，在执行业务代码出错时可以抛出此类异常，每个业务模块都需要继承该类，形成针对特定业务的异常
 * @author meteor
 */
@Data
public class ServiceException extends Exception {

    /**
     * 错误码
     */
    private long errorCode;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 构造函数-通过errorCode和errorMsg构造异常对象
     * @param errorCode 错误码
     * @param errorMsg 错误信息
     */
    public ServiceException(long errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 构造函数-通过IErrorCode对象构造异常对象
     * @param errorCode
     */
    public ServiceException(IErrorCode errorCode) {
        this(errorCode.code(), errorCode.msg());
    }
}
