package com.company.project.core;

import lombok.Data;

/**
 * @author shanchao
 * @date 2018-04-23
 */
@Data
public class CommonResp {

    private String msg;

    private CommonResp(String msg) {
        this.msg = msg;
    }

    private static CommonResp SUCCESS = new CommonResp("SUCCESS");

    public static CommonResp success() {
        return SUCCESS;
    }

    public static CommonResp fail(String msg) {
        return new CommonResp(msg);
    }
}
