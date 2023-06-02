package com.baloot.controller;

import com.baloot.service.BalootSystem;
import com.baloot.service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Enumeration;

import static java.lang.Thread.sleep;

@WebFilter(urlPatterns ={
        "/commodity",
        "/",
        "/user",
        "/providers",
        "/commodities"
})
@Component
public class JWTController extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("authorization");
        if(jwt == null) {
            response.sendError(403, "not Logged In");
            return;
        }
        if(!jwt.equals("null")){
            Claims claims = JWTService.getInstance().decodeJWT(jwt);
            if(JWTService.getInstance().validateJwt(claims)){
                request.setAttribute("username", claims.get("username"));
                filterChain.doFilter(request, response);
                return;
            }
        }
        response.sendError(403, "UnAuthorized");
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        if ((!path.contains("/commodities")&&!path.contains("/user")&&!path.contains("/providers")&&!path.contains("/comment"))  || "OPTIONS".equals(request.getMethod())) {
            return true;
        }
        return false;
    }
}
