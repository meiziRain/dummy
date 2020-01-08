package com.meizi.admin.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @program: dummy
 * @description: 返回结果封装类
 * @author: Meizi
 * @create: 2020-01-06 11:18
 **/

public class Result<T> implements Serializable {

    /**成功**/
    static  final  String SUCCESS="0";
    /**失败**/
    static final  String ERROR="1";
    /**
     * 数据
     */
    private T data;
    /**
     * 返回编码
     */
    private String resultCode;
    /**
     * 返回消息
     */
    private String resultMsg;
    /**
     * 错误消息
     */
    private String errorMsg;
    /**
     * 服务器返回时间戳
     */
    private long sysTime;

    public static <T> Result<T> succeed(String msg) {
        return succeedWith(null, SUCCESS, msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return succeedWith(model, SUCCESS, msg);
    }

    public static <T> Result<T> succeed(T model) {
        return succeedWith(model, SUCCESS, "");
    }

    public static <T> Result<T> succeedWith(T data, String code, String msg) {
        return new Result<>(data, code, msg,"", System.currentTimeMillis());
    }

    public static <T> Result<T> failed(String msg) {
        return failedWith(null, ERROR, msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return failedWith(model, ERROR, msg);
    }

    public static <T> Result<T> failedWith(T data, String code, String msg) {
        return new Result<>(data, code, msg, msg, System.currentTimeMillis());
    }

    public static boolean isSuccess(Result result) {
        if(result != null && StringUtils.isNotEmpty(result.getResultCode())
                && SUCCESS.equals(result.getResultCode())){
            return true;
        }else{
            return false;
        }
    }

    public Result(T data, String resultCode, String resultMsg, String errorMsg, long sysTime) {
        this.data = data;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.errorMsg = errorMsg;
        this.sysTime = sysTime;
    }

    public Result() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public long getSysTime() {
        return sysTime;
    }

    public void setSysTime(long sysTime) {
        this.sysTime = sysTime;
    }
}
