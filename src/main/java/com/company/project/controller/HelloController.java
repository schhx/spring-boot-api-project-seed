package com.company.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.company.project.core.exception.BaseException;
import com.company.project.core.exception.ForbiddenException;
import com.company.project.core.security.LoginNeedless;
import com.company.project.core.validation.PropMin;
import com.company.project.core.validation.PropNotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author shanchao
 * @date 2018-05-17
 */
@RestController
@Validated
public class HelloController {

    @GetMapping("/e1")
    @LoginNeedless
    public void e1() {
        throw new ForbiddenException();
    }

    @GetMapping("/e2")
    @LoginNeedless
    public void e2() {
        throw new BaseException("dadasd");
    }

    @PostMapping("/name")
    @LoginNeedless
    public String name(@RequestBody
                       @PropNotEmpty(value = "name")
                       @PropMin(value = "age", min = 18, nullable = false)
                               JSONObject json) {
        return json.getString("name");
    }

    @GetMapping("/date")
    @LoginNeedless
    public Date date(Date date){
        return date;
    }

    @PostMapping("/date")
    @LoginNeedless
    public Date date(@RequestBody DateVO vo){
        return vo.getDate();
    }

}
