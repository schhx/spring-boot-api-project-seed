package com.company.project.exception;

public abstract class BaseException extends RuntimeException {

    private boolean needLog;

    public BaseException(String message) {
        super(message);
        this.needLog = false;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.needLog = true;
    }

    public boolean isNeedLog() {
        return needLog;
    }
}
