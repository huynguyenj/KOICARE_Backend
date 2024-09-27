package group7.se1876.kcs_backend.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255)")
    private String userName;

    private String password;

    private Long phone;

    private String email;

    private boolean status;


    private Set<String> roles;

}
