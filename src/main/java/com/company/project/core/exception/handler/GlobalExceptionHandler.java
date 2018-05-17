package com.company.project.core.exception.handler;

import com.company.project.core.exception.BaseException;
import com.company.project.core.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author shanchao
 * @date 2018-04-30
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorVO handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMsg = errorFieldToString(fieldErrors);
        return ErrorVO.of(errorMsg);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorVO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMsg = errorFieldToString(fieldErrors);
        return ErrorVO.of(errorMsg);
    }

    private String errorFieldToString(List<FieldError> fieldErrors) {
        StringJoiner stringJoiner = new StringJoiner(";");
        for (FieldError fieldError : fieldErrors) {
            stringJoiner.add(fieldError.getDefaultMessage());
        }
        return stringJoiner.toString();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorVO handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> set = e.getConstraintViolations();
        StringJoiner stringJoiner = new StringJoiner(";");
        for (ConstraintViolation constraintViolation : set) {
            stringJoiner.add(constraintViolation.getMessage());
        }
        String errorMsg = stringJoiner.toString();
        return ErrorVO.of(errorMsg);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            NoHandlerFoundException.class, MissingPathVariableException.class,
            HttpMessageNotReadableException.class, HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class
    })
    public ErrorVO handleException(Exception e) {
        if (e instanceof NoHandlerFoundException) {
            return ErrorVO.of("请求路由不存在", e.getMessage());
        } else if (e instanceof MissingPathVariableException) {
            return ErrorVO.of("缺少路径参数", e.getMessage());
        } else if (e instanceof HttpMessageNotReadableException) {
            return ErrorVO.of("请输入正确的参数", e.getMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return ErrorVO.of("HTTP请求方法错误", e.getMessage());
        } else if(e instanceof MissingServletRequestParameterException){
            return ErrorVO.of("缺少必填参数", e.getMessage());
        }
        return ErrorVO.of(e.getMessage(), e.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public ErrorVO handleException(HttpServletRequest request, HttpServletResponse response, BaseException e) {
        ResponseStatus status = e.getClass().getAnnotation(ResponseStatus.class);
        if(status != null){
            response.setStatus(status.value().value());
        } else {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        }
        if (e.isNeedLog()) {
            logError(request, e);
        }
        return ErrorVO.of(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorVO handleException(HttpServletRequest request, Exception e) {
        logError(request, e);
        return ErrorVO.of("未知异常", e.getMessage());
    }

    private void logError(HttpServletRequest request, Exception e) {
        StringBuilder messageBuilder = new StringBuilder("Unhandled exception in request:\n");
        messageBuilder.append(request.getMethod());
        messageBuilder.append(" ");
        messageBuilder.append(request.getRequestURI());
        messageBuilder.append("\nHeader:");
        toIterable(request::getHeaderNames).forEach(name -> {
            messageBuilder.append("\n");
            String value = request.getHeader(name);
            messageBuilder.append(name);
            messageBuilder.append(": ");
            messageBuilder.append(value);
        });
        messageBuilder.append("\n");
        if (request.getParameterMap().size() > 0) {
            StringBuilder parameters = getRequestParameters(request);
            messageBuilder.append(parameters);
        }
        log.error(messageBuilder.toString(), e);
    }

    private StringBuilder getRequestParameters(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder("request parameters:");
        stringBuilder.append("\n");
        final Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((k, v) -> {
            stringBuilder.append(k);
            stringBuilder.append(": ");
            stringBuilder.append(Arrays.toString(v));
            stringBuilder.append("\n");
        });
        return stringBuilder;
    }

    private <T> Iterable<T> toIterable(Supplier<Enumeration<T>> enumerationSupplier) {
        return () -> CollectionUtils.toIterator(enumerationSupplier.get());
    }
}
