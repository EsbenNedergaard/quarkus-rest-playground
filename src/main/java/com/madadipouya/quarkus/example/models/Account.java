package com.madadipouya.quarkus.example.models;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users") // TODO: replace
@UserDefinition
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false)
    @Username
    private String username;

    @Column(name = "password", nullable = false)
    @Password
    @JsonbTransient
    private String password;

    @Column(name = "role", nullable = false)
    @Roles
    private String role;

    @Column(name = "first_name", nullable = false)
    @NotBlank
    @Size(max = 256)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    @Size(max = 256)
    private String lastName;

    @Column(name = "age", nullable = false)
    @Min(1)
    @Max(200)
    private int age;
}
