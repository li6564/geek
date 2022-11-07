package cn.lico.geek.core;

/**
 * 错误码接口
 * @author linan
 */
public interface IErrorCode {

    /**
     * 获取错误代码
     * @return
     */
    long code();

    /**
     * 获取错误信息
     * @return
     */
    String msg();

}
