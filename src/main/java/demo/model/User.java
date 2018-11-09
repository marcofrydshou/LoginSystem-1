package demo.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;


/**
 * User Entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "token")
    private String token;

    @Column(name = "token_date")
    private LocalDateTime tokenDate;
/*
    @Transient
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public void hashPassword(){
        this.password = encoder.encode(this.password);
    }*/
/*
    public User(String username, String password, boolean enabled, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.email = email;
    }

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }*/
}
