package com.example.webapplication.service;

import com.example.webapplication.DTO.request.ChangePasswordRequest;
import com.example.webapplication.DTO.request.SignupRequest;
import com.example.webapplication.jwt.JwtUtils;
import com.example.webapplication.model.User;
import com.example.webapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtUtils jwtUtils;
    public Integer sendMailForVerify(String email){
        User user=userRepository.findByEmail(email);
        if (user!=null) {
            if(user.getIsVerify()==null){
                String token=tokenService.createToken(user.getId().toString());
                user.setVerify_token(token);
                emailService.sendMailForVerify(token,email);
                userRepository.save(user);
                return 1;
            }
            return 0;
        }
        return -1;
    }
    public void addUserForSignup(SignupRequest signupRequest){
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        user.setRole(roleService.setUser());
        user=userRepository.save(user);
        String token=tokenService.createToken(user.getId().toString());
        user.setVerify_token(token);
        emailService.sendMailForVerify(token,signupRequest.getEmail());
        userRepository.save(user);
    }
    public boolean updateVerify(String token){
        if (token!=null){
            User user=userRepository.findByVerifyToken(token);
            if (user!=null){
                user.setVerify_token(null);
                user.setIsVerify(1);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
    public boolean updateResetPasswordToken(String email) {
            User user = userRepository.findByEmail(email);
            if (user!=null){
                String token=tokenService.createToken(user.getId().toString());
                user.setResetPasswordToken(token);
                userRepository.save(user);
                emailService.sendMailForResetPassword(token,email);
                return true;
            }
            return false;
    }
    public boolean updatePassword(String token, String newPassword) {
        if (token!=null){
            String encodedPassword = encoder.encode(newPassword);
            User user=userRepository.findByResetPasswordToken(token);
            if (user!=null){
                user.setPassword(encodedPassword);
                user.setResetPasswordToken(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
    public boolean existUsername(String username){
        return userRepository.existsByUsername(username);
    }
    public boolean existEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public boolean changePasswordByJWT(String token, ChangePasswordRequest changePasswordRequest){
        String username=jwtUtils.getUserNameFromJwtToken(token);
        if(username!=null){
            User user=userRepository.findByUsername(username);
            if (user!=null){
                if(encoder.matches(changePasswordRequest.getCurrent_password(), user.getPassword())){
                    user.setPassword(encoder.encode(changePasswordRequest.getNew_password()));
                    userRepository.save(user);
                    return true;
                }
            }
        }
        return false;
    }
    public User getProfileUser(String username){
        User user=userRepository.findByUsername(username);
        return user;
    }
}
