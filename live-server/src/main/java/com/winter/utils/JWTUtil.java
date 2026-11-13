package com.winter.utils;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @author Mr_Li
 */
public class JWTUtil {
    //token密钥
    private static final String TOKEN_SECRET = "ZY78C6Sd3v";
    private static final long EXPIRE_TIME = 12L * 3600L * 1000L;

    /**
     * 创建token
     */
    public static <T>String sign(T data) {
        Date date;
        date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        String jsonStr = JSON.toJSONString(data);
        return JWT.create()
                .withClaim("body", jsonStr)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    public static <T>Object getPayload(String token, Class<T> clazz) {
        DecodedJWT jwt = JWT.decode(token);
        String jsonStr = jwt.getClaim("body").asString();
        Object result = JSON.parseObject(jsonStr, clazz);
        return result;
    }

    public static Long getExpireTime(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Long exp = jwt.getExpiresAt().getTime();
            return exp;
        } catch (JWTDecodeException e) {
            return null;
        }

    }

    public static boolean verify(String token) {
        if(token == null) {
            return false;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            //效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
