package com.example.webapplication.DTO.response;

import com.example.webapplication.model.Role;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class JwtResponse {
	private int status;
	private Date timestamp;
	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String email;
	private String roles;
	private String message;

	public JwtResponse(String accessToken, Long id, String username, String email, String roles) {
		this.status=0;
		this.timestamp=new Date();
		this.message="User login successfully";
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

}
