package demo.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "token_date")
    private LocalDateTime tokenDate;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Role> authoritites;

    public boolean hasAuthority(String authorityName){
        return (authoritites.stream().anyMatch(auth -> auth.getAuthority().toLowerCase().equals(authorityName.toLowerCase())));
    }

    public void addAuthority(Role role){
        this.authoritites.add(role);
    }

//    @Transient
//    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public void hashPassword(){
//        this.password = encoder.encode(this.password);
//    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
