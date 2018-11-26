package demo.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User Entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;


    @Column(name = "address")
    private String address;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<Role> authoritites;

    public boolean hasAuthority(String authorityName){
        return (authoritites.stream().anyMatch(auth -> auth.getAuthority().toLowerCase().equals(authorityName.toLowerCase())));
    }


    public void addAuthority(Role role){
        this.authoritites.add(role);
    }

    public void removeAuthority(String role) {
        // removeIf() it removes all the elements from the list which the same with given a
        this.authoritites.removeIf(a -> a.getAuthority().equals(role));
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public User(long id, String username, String password,  String email, boolean enabled, List<Role> authoritites){
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.authoritites = authoritites;
    }

    public User(String username, String password,  String email, boolean enabled, List<Role> authoritites){
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.authoritites = authoritites;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authoritites;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
