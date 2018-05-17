package com.company.project.core.security;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.company.project.core.exception.BaseException;
import com.company.project.core.exception.UnauthorizedException;
import com.company.project.core.security.Subject;

import java.util.Date;

/**
 * @author shanchao
 * @date 2018-05-16
 */
public class JwtUtil {

    private static final String SECRET = "secret";

    private static final Long EXPIRE_TIME = 24 * 60 * 60 * 1000L;

    public static String encodeSubject(Subject subject) {
        return encodeJwt(JSON.toJSONString(subject), new Date(System.currentTimeMillis() + EXPIRE_TIME));
    }

    public static Subject decodeSubject(String token) {
        return JSON.parseObject(decodeJwt(token), Subject.class);
    }

    public static String encodeJwt(String data) {
        return encodeJwt(data, new Date(System.currentTimeMillis() + EXPIRE_TIME));
    }

    public static String encodeJwt(String data, Date expireAt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            String token = JWT.create()
                    .withIssuedAt(new Date())
                    .withExpiresAt(expireAt)
                    .withClaim("data", data)
                    .sign(algorithm);
            return token;
        } catch (Exception e) {
            throw new BaseException("签名失败", e);
        }
    }


    public static String decodeJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("data").asString();
        } catch (TokenExpiredException e) {
            throw new UnauthorizedException("Token过期");
        } catch (JWTVerificationException exception) {
            throw new UnauthorizedException("Token无效");
        } catch (Exception e) {
            throw new BaseException("验签失败", e);
        }
    }


    public static void main(String[] args) {
        String token = encodeJwt("Hello world");
        System.out.println(token);
        System.out.println(decodeJwt(token));
    }
}
