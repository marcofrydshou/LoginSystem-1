package demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * User Entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "users")
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Transient
    private String confirmpassword;

    @Column(name = "address")
    private String address;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "token")
    private String token;

    @Column(name = "token_date")
    private LocalDateTime tokenDate;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Role> authoritites;

    public boolean hasAuthority(String authorityName){
        return (authoritites.stream().anyMatch(auth ->  auth.getAuthority().toLowerCase().equals(authorityName.toLowerCase())));
    }



    public void addAuthority(Role role) {
        this.authoritites.add(role);

    }
}
