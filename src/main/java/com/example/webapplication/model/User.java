package com.example.webapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(	name = "users",
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String username;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	@Column(name = "reset_password_token")
	private String resetPasswordToken;
	@Column(name = "is_verify")
	private Integer isVerify;
	@Column(name = "verify_token")
	private String verify_token;
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

}
