package com.example.webapplication.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 20)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "roles_permissoons",
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<Permission> permissions=new HashSet<>();

}