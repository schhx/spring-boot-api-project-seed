package com.company.project.core.interceptor;

import com.company.project.core.exception.ForbiddenException;
import com.company.project.core.exception.UnauthorizedException;
import com.company.project.core.security.LoginNeedless;
import com.company.project.core.security.RequirePermission;
import com.company.project.core.security.RequireRole;
import com.company.project.core.security.Subject;
import com.company.project.core.security.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 认证拦截器
 *
 * @author shanchao
 * @date 2018-05-16
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        LoginNeedless loginNeedless = method.getMethodAnnotation(LoginNeedless.class);
        if (loginNeedless != null) {
            return true;
        }

        String headToken = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(headToken)) {
            headToken = headToken.substring("Bearer ".length());
        } else {
            throw new UnauthorizedException("缺少Token");
        }
        Subject subject = JwtUtil.decodeSubject(headToken);
        if (!checkRole(method, subject.getRoles())
                || !checkPermission(method, subject.getPermissions())) {
            throw new ForbiddenException();
        }

        return true;
    }

    private boolean checkRole(HandlerMethod method, List<String> existRoles) {
        RequireRole requireRole = method.getMethodAnnotation(RequireRole.class);
        if (null == requireRole) {
            return true;
        }
        String[] roles = requireRole.value();
        for (String role : roles) {
            if (existRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPermission(HandlerMethod method, List<String> existPermissions) {
        RequirePermission requirePermission = method.getMethodAnnotation(RequirePermission.class);
        if (null == requirePermission) {
            return true;
        }
        String[] permissions = requirePermission.value();
        for (String role : permissions) {
            if (existPermissions.contains(role)) {
                return true;
            }
        }
        return false;
    }
}
