package com.company.project.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author shanchao
 * @date 2018-05-16
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BaseException extends RuntimeException {

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
