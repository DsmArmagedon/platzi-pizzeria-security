package com.platzi.pizzeria.persitence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="user_role")
@IdClass(UserRoleId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleEntity {
    @Id
    @Column(nullable = false, length = 20)
    private String username;

    @Id
    @Column(nullable = false, length = 20)
    private String role;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime granted_date;

    @ManyToOne
    @JoinColumn(name="username", referencedColumnName = "username", insertable = false, updatable = false)
    private UserEntity user;
}
