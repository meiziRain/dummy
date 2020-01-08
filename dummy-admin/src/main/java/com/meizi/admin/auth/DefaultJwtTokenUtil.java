package com.meizi.admin.auth;

/**
 * @program: dummy
 * @description:
 * @author: Meizi
 * @create: 2020-01-06 15:56
 **/

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT操作类
 */
public class DefaultJwtTokenUtil {

    /**
     * 日期操作
     **/
    private Clock clock = DefaultClock.INSTANCE;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.prefix}")
    private String tokenPrefix;

    /**
     * 距离过期时间的刷新时间：如过期时间三十分钟，刷新时间十分钟，则在（30-10）  二十分钟后会刷新token
     **/
    @Value("${jwt.refreshTime}")
    private Long refreshTime;

    /**
     * 从token获取用户名称
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从token获取用户ID
     *
     * @param token
     * @return
     */
    public String getIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    /**
     * 获取生成日期
     *
     * @param token
     * @return
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Boolean isTimeToRefresh(String token) {
        Date refreshDate = new Date(getExpirationDateFromToken(token).getTime() - refreshTime);
        return refreshDate.before(clock.now());
    }

    /**
     * 获取过期日期
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断是否过期
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    /**
     * 其它控制逻辑(判断token是否可以刷新， 如token已过期的不能刷新， 修改过密码的不能刷新等场景)
     *
     * @param token
     * @return
     */
    private Boolean ignoreTokenExpiration(String token) {
        return true;
    }

    /**
     * 生成token
     *
     * @param userDetails
     * @return
     */
    public String generateToken(DefaultJwtUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails);
    }

    private String doGenerateToken(Map<String, Object> claims, DefaultJwtUserDetails userDetails) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setId(userDetails.getId().toString())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 判断token是否可以刷新
     *
     * @param token
     * @return
     */
    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 校验token
     *
     * @param token 用户token信息
     * @return
     */
    public Boolean validateToken(String token) {
        //  获取token创建时间
        //final Date created = getIssuedAtDateFromToken(token);
        return (!isTokenExpired(token));
    }

    /**
     * 计算得到过期日期
     *
     * @param createdDate
     * @return
     */
    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration);
    }

    /**
     * 获取token头信息
     *
     * @return
     */
    public String getTokenHeader() {
        return tokenHeader;
    }

    /**
     * 获取token前缀信息
     *
     * @return
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

    /**
     * 获取tokenId
     *
     * @return
     */
    public String getTokenId() {
        String value = StringUtils.EMPTY;
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();

        if (null == attributes) {
            return value;
        }

        HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();

        if (null == request) {
            return value;
        }
        value = request.getHeader(getTokenHeader());

        if (StringUtils.isBlank(value)) {
            return value;
        }

        // 截取真正的token信息
        return value.substring(getTokenPrefix().length());
    }
}
