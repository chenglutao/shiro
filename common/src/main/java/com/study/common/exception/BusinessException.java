package com.study.common.exception;

import com.study.common.entity.Key;

/**
 * @author chenglutao
 */

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -1938667033736643289L;

    private Key key;

    /**
     * 异常msg
     */
    private String msg;

    protected BusinessException() {
    }

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(Key key, String msg) {
        super();
        this.key = key;
        this.msg = msg;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
