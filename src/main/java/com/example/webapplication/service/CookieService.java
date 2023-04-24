package com.example.webapplication.service;

import com.example.webapplication.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    @Autowired
    private JwtUtils jwtUtils;
    public static void resetCookie(HttpServletRequest request, HttpServletResponse response){
        request.getSession().invalidate();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }
    public boolean checkJwt(String jwt,HttpServletRequest request, HttpServletResponse response){
        if (jwtUtils.validateJwtToken(jwt)){
            return true;
        }
        resetCookie(request,response);
        return false;
    }
}
