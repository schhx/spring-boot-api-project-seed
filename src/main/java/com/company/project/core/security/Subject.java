package com.company.project.core.security;

import lombok.Data;

import java.util.List;

/**
 * 登录主体的信息
 *
 * @author shanchao
 * @date 2018-05-17
 */
@Data
public class Subject {

    private List<String> roles;

    private List<String> permissions;
}
