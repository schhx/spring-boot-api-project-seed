package com.company.project.exception.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MyErrorController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public ErrorVO error(HttpServletRequest request){
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Throwable throwable = errorAttributes.getError((WebRequest) requestAttributes);
        return ErrorVO.of(throwable.getMessage());
    }
}
