package com.winter.filter;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.winter.common.pojo.Response;
import com.winter.common.pojo.ResponseType;
import com.winter.common.pojo.UserToken;
import com.winter.utils.JWTUtil;
import com.winter.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 过滤器，校验接口权限
 */
@Slf4j
@Order(1)
@WebFilter(filterName = "authFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    @Value("${app.swagger}")
    private Boolean swaggerFlag;

    //白名单接口集合
    private final List<String> whiteList = Lists.newArrayList(
            "/api/401",
            "/api/notlogin",
            "/api/upload",
            "/api/login",
            "/api/404",
            "/api/code",
            "/api/doc.html",
            "/api/swagger-ui.html",
            "/api/live/group/list",
            "/api/live/publish",
            "/api/live/unpublish",
            "/api/live/play",
            "/api/live/stop",
            "/api/live/dvrs",
            "/api/live/page",
            "/api/live/dvr/page"
    );

    private String getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        if (Pattern.matches("^/api/websocket/[a-zA-Z0-9]+", uri)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (swaggerFlag && (uri.contains("/getVerify") || uri.contains("/webjars") || uri.contains("/api-docs") || uri.contains("/swagger") || uri.contains("/feedback"))) {
            filterChain.doFilter(request, response);
            return;
        }
        if (whiteList.contains(uri)) {
            filterChain.doFilter(request, response);
            return;
        }
        if(Pattern.matches("^/api/(public|uploads|dvrs)/.+", uri)){
            // /api/public，/api/uploads， /api/dvrs不需要鉴权
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");
        if (ObjectUtil.isEmpty(token)) {
            ResponseUtil.responseJson(response, 200, Response.failed(ResponseType.NOT_LOGIN));
            return;
        } else if (!JWTUtil.verify(token)) {
            ResponseUtil.responseJson(response, 200, Response.failed(ResponseType.NOT_LOGIN));
            return;
        } else {
            try{
                UserToken userToken = (UserToken)JWTUtil.getPayload(token, UserToken.class);
                request.setAttribute("user", userToken);
            }catch (Exception e){
                ResponseUtil.responseJson(response, 200, Response.failed(ResponseType.NOT_LOGIN));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
