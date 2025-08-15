package com.tech.springSecurity.entities;

import java.util.prefs.BackingStoreException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    private String name;

    public enum RoleName {
        ADMIN(1L),
        BASIC(2L);
        

        long roleId;

        RoleName(long roleId) {
            this.roleId = roleId;
        }
    }
}
