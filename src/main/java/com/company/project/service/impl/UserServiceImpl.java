package com.company.project.service.impl;

import com.company.project.dao.UserMapper;
import com.company.project.model.User;
import com.company.project.service.UserService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


/**
* @author CodeGenerator
* @date 2018-08-08
*/
@Service
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Autowired
    private UserMapper userMapper;

}
