package com.denisenko.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
