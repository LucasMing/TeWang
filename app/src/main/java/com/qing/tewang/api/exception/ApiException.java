package com.qing.tewang.api.exception;

/**
 * Created by wuliao on 2018/4/17.
 */

public class ApiException extends Exception {

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public ApiException(Throwable throwable, String message) {
        super(throwable);
        this.message = message;
    }


}
