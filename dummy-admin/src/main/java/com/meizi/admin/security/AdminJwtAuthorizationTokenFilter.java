package com.meizi.admin.security;

import com.meizi.admin.auth.DefaultJwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AdminJwtAuthorizationTokenFilter extends OncePerRequestFilter {
    /**
     * 用户操作类
     */
    private final UserDetailsService userDetailsService;
    /**
     * jwtToken工具类
     */
    private final DefaultJwtTokenUtil jwtTokenUtil;
    /**
     * tokenHeader设计
     */
    private final String tokenHeader;
    /**
     * 头部token前缀
     */
    private final String tokenPrefix;

    /**
     * 构造函数
     * @param userDetailsService
     * @param jwtTokenUtil
     * @param tokenHeader
     */
    public AdminJwtAuthorizationTokenFilter(@Qualifier("adminJwtUserDetailsService") UserDetailsService userDetailsService,
                                            @Qualifier("jwtTokenUtil") DefaultJwtTokenUtil jwtTokenUtil,
                                            @Value("${jwt.header}") String tokenHeader,
                                            @Value("${jwt.prefix}") String tokenPrefix) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeader = tokenHeader;
        this.tokenPrefix = tokenPrefix;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestHeader = request.getHeader(this.tokenHeader);
        String username = null;
        String authToken = null;
        if (StringUtils.isNotBlank(requestHeader) && requestHeader.startsWith(tokenPrefix)) {
            try {
                authToken = requestHeader.substring(tokenPrefix.length());
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
            }
        }

        if (StringUtils.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            // 检验token信息, 现只校验了token是否过期, 若需要增加其它校验在validateToken中增加即可
            if (jwtTokenUtil.validateToken(authToken)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authorizated user '{}', setting security context", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                if(jwtTokenUtil.isTimeToRefresh(authToken)){
                    // 更新token
                    String newToken = jwtTokenUtil.refreshToken(authToken);
                    // 将tokenHeader显示在headers，不然setHeader不生效
                    response.setHeader("Access-Control-Expose-Headers",this.tokenHeader);
                    response.setHeader(this.tokenHeader, newToken);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
