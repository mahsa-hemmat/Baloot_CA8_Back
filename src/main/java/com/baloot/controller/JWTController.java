package com.baloot.controller;

import com.baloot.service.BalootSystem;
import com.baloot.service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns ={
        "/commodity"
})
public class JWTController extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String[] split = header.split(" ", 2);
        String jwt = split[1];



        if(jwt != null && !jwt.equals("null")){
            Claims claims = JWTService.getInstance().decodeJWT(jwt);
            if(JWTService.getInstance().validateJwt(claims)){
                request.setAttribute("username", claims.get("username"));
                filterChain.doFilter(request, response);
                return;
            }
        }
        response.sendError(403, "UnAuthorized");
    }
}
