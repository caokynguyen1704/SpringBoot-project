package com.example.webapplication.jwt;


import com.example.webapplication.model.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class JwtUtils {

  private String jwtSecret="keyjwt";


  private int jwtExpirationMs=24*60*60;

  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    String md5Password = DigestUtils.md5DigestAsHex(userPrincipal.getPassword().getBytes());
    return Jwts.builder().setSubject((userPrincipal.getUsername())).claim("key", md5Password).setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }
  public String getClaimKeyFromJwt(String jwt) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody();
    return claims.get("key", String.class);
  }
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    }catch (Exception e){
    }

    return false;
  }
  public String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    if (headerAuth==null){
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if (cookie.getName().equals("Authorization")) {
            headerAuth = "Bearer "+cookie.getValue();
            break;
          }
        }
      }
    }
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7, headerAuth.length());
    }

    return null;
  }

}
