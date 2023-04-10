package com.example.webapplication.controller;

import com.example.webapplication.DTO.request.*;
import com.example.webapplication.DTO.response.JwtResponse;
import com.example.webapplication.DTO.response.MessageResponse;
import com.example.webapplication.jwt.JwtUtils;
import com.example.webapplication.model.User;
import com.example.webapplication.model.UserDetailsImpl;
import com.example.webapplication.repository.UserRepository;
import com.example.webapplication.service.TokenService;
import com.example.webapplication.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/guest")
public class NonUserController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/signup")
	public ResponseEntity<?> addUser(@Valid @RequestBody SignupRequest signupRequest){
		if (userService.existUsername(signupRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(400,"Username is already taken!"));
		}

		if (userService.existEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(400,"Email is already in use!"));
		}

		userService.addUserForSignup(signupRequest);
		return ResponseEntity.ok(new MessageResponse(0,"Registered successfully!"));
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			Cookie jwtCookie = new Cookie("Authorization",jwt);
			jwtCookie.setMaxAge(3600);
			jwtCookie.setPath("/");
			response.addCookie(jwtCookie);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return ResponseEntity.ok(new JwtResponse(jwt,
					userDetails.getUser().getId(),
					userDetails.getUsername(),
					userDetails.getUser().getEmail(),
					roles.get(0)));
		}catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(400, "Invalid username or password"));
		}
	}

	@PostMapping("/forget-password")
	public ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest, HttpServletRequest request) {
		if(userService.updateResetPasswordToken(forgetPasswordRequest.getEmail())){
			return ResponseEntity.ok(new  MessageResponse(0,"Done"));
		};
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(400, "Wrong Email"));

	}

	@PostMapping("/reset-password/{token}")
	public ResponseEntity<?> resetPassword(@PathVariable String token,@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

		if(tokenService.validateToken(token)){
			if(userService.updatePassword(token,resetPasswordRequest.getNew_password())){
				return ResponseEntity.ok(new MessageResponse(0,"Pass change!!!"));
			};
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(400,"Wrong Token"));
	}

	@PostMapping("/verify")
	public ResponseEntity<?> sendVerify(@Valid @RequestBody VerifyRequest verifyRequest, HttpServletRequest request) {
		try{
			Integer data=userService.sendMailForVerify(verifyRequest.getEmail());
			if(data==0){
				return ResponseEntity.ok(new  MessageResponse(1,"Has been successfully verified in the past"));
			} else if (data==1){
				return ResponseEntity.ok(new  MessageResponse(0,"Successfully"));
			}else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(400, "Wrong Email"));
			}

		}catch (Exception e){
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(400, "Error"));
		}
	}
	@GetMapping("/verify/{token}")
	public ResponseEntity<?> verify(@PathVariable String token) {
		if(tokenService.validateToken(token)){
			if(userService.updateVerify(token)){
				return ResponseEntity.ok(new MessageResponse(0,"Verify!!!"));
			};
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(400,"Wrong Token"));
	}
	@PostMapping("/profile/{username}")
	public ResponseEntity<?> getProfile(@PathVariable String username) {
		return ResponseEntity.ok(userService.getProfileUser(username));
	}
}
