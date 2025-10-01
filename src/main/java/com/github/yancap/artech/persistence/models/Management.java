package com.github.yancap.artech.persistence.models;

import com.github.yancap.artech.utils.enums.RoleAccess;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "managements")
@Data
public class Management {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String role = RoleAccess.Author.name();

    @Column(name = "url_avatar")
    private String urlAvatar = "";

    @OneToMany(targetEntity=Article.class, mappedBy = "manager")
    private List<Article> articles;
}
