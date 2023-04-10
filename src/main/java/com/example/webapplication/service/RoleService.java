package com.example.webapplication.service;

import com.example.webapplication.model.Role;
import com.example.webapplication.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EntityManager entityManager;
    public boolean existName(String name){
        return roleRepository.existsByName(name);
    }
    public Role setUser(){
        return roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("USER not found"));
    }
    public Role setAdmin(){
        return roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("ADMIN not found"));
    }

    public void addRole(String name){
        if (!(roleRepository.existsByName(name))){
            Role role=new Role();
            role.setId(this.getMaxRoleId()+1);
            role.setName(name);
            roleRepository.save(role);
        }
    }
    public Integer getMaxRoleId() {
        TypedQuery<Integer> query = entityManager.createQuery("SELECT MAX(r.id) FROM Role r", Integer.class);
        return query.getSingleResult();
    }
}
