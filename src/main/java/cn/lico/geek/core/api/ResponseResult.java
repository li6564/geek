package cn.lico.geek.core.api;

import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private String code;
    private T data;
    private String message;

    public ResponseResult() {
        this.code = AppHttpCodeEnum.SUCCESS.getMsg();
    }
    public ResponseResult(String message,String code){
        this.message = message;
        this.code = code;
    }
    public ResponseResult(T data) {
        this.data = data;
        this.code = AppHttpCodeEnum.SUCCESS.getMsg();
    }

    public ResponseResult(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(String code) {
        this.code = code;
    }

    public static ResponseResult errorResult(String message) {
        ResponseResult result = new ResponseResult();
        return result.error(message);
    }
    public static ResponseResult okResult() {
        ResponseResult result = new ResponseResult();
        return result;
    }


    public static ResponseResult okResult(String code) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null);
    }

    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums){
        return okResult(enums.getMsg());
    }


    public ResponseResult<?> error(String message) {
        this.message = message;
        return this;
    }

    public ResponseResult<?> ok(String code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }


    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }



}