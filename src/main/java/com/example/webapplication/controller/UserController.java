package com.example.webapplication.controller;

import com.example.webapplication.DTO.request.ChangePasswordRequest;
import com.example.webapplication.DTO.response.MessageResponse;
import com.example.webapplication.jwt.JwtUtils;
import com.example.webapplication.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @PostMapping("/change-password")
    public ResponseEntity<?> changePass(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request){
        try {
            String jwt = jwtUtils.parseJwt(request);
            if(jwt !=null){
                if(jwtUtils.validateJwtToken(jwt)){
                    if(userService.changePasswordByJWT(jwt,changePasswordRequest)){
                        return ResponseEntity.ok(new  MessageResponse(0,"Password change!!"));
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(400, "Lỗi xác thực"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(400, "Lỗi xác thực"));
        }
    }
    @PostMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request){
        try {
            String jwt = jwtUtils.parseJwt(request);
            if(jwt !=null){
                if(jwtUtils.validateJwtToken(jwt)){
                    return ResponseEntity.ok(userService.getProfileUser(jwtUtils.getUserNameFromJwtToken(jwt)));
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(400, "Lỗi xác thực"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(400, "Lỗi xác thực"));
        }
    }
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
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
        return ResponseEntity.ok(new MessageResponse(0,"You are logout"));
//        return "redirect:/login";
    }
}
