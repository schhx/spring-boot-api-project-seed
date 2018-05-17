package com.company.project.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 授权异常
 *
 * @author shanchao
 * @date 2018-05-17
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends BaseException {

    public ForbiddenException() {
        super("对不起，您没有权限访问");
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
