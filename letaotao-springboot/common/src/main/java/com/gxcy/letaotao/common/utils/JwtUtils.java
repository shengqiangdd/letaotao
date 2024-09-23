package com.gxcy.letaotao.common.utils;

import com.gxcy.letaotao.common.entity.BaseUser;
import com.gxcy.letaotao.common.exception.BizException;
import com.gxcy.letaotao.common.exception.BizExceptionEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtUtils {
    //秘钥
    private String secret;
    //过期时间
    private Long expiration;

    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成令牌
     *
     * @param user 用户
     * @return 令牌
     */
    public String generateToken(BaseUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.SUBJECT, LTConstants.USER_INFO);
        claims.put(LTConstants.USER_ID, user.getId());
        claims.put(LTConstants.USER_NAME, user.getUsername());
        claims.put(Claims.ISSUED_AT, new Date());
        return generateToken(claims);
    }

    /**
     * 生成令牌
     *
     * @param user       用户
     * @param expiration 过期时间
     * @return 令牌
     */
    public String generateTokenWithExpiration(BaseUser user, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(LTConstants.USER_INFO)
                .claim(LTConstants.USER_ID, user.getId())
                .claim(LTConstants.USER_NAME, user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    /**
     * 将天数转换为毫秒
     *
     * @param days 天数
     * @return 毫秒
     */
    public long getExpirationDurationInMillis(long days) {
        return days * 24 * 60 * 60 * 1000L; // 将天数转换为毫秒
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.get(LTConstants.USER_NAME).toString();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 刷新令牌
     *
     * @param token 令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(Claims.ISSUED_AT, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @param user  用户
     * @return 是否有效
     */
    public Boolean validateToken(String token, BaseUser user) {
        String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 通过Token解析出Claims
     *
     * @param token
     * @return
     */
    public Claims parseToken(String token) {

        if (token == null) {
            throw new BizException(BizExceptionEnum.USER_NOT_LOGIN);
        }

        try {
            Jws<Claims> claimsJws = Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (ExpiredJwtException e) {
            throw new BizException(BizExceptionEnum.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new BizException(BizExceptionEnum.TOKEN_INVALID);
        }
    }

}
