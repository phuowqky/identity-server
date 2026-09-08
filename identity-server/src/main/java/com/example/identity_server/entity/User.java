package com.example.identity_server.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@SequenceGenerator(
        name = "seq_generator",
        sequenceName = "USERS_SEQ",
        allocationSize = 1
)
@Getter
@Setter
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    private Integer age;

    @Column(nullable = false)
    private Integer enabled = 1;

    @Column(name = "don_vi_id")
    private Long donViId;
}