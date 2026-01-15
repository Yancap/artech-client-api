package com.github.yancap.artech.client.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name = "ClientUser")
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String urlAvatar;

    @OneToMany(targetEntity=Comment.class, mappedBy = "user", cascade = { CascadeType.ALL })
    private List<Comment> comments;

}
