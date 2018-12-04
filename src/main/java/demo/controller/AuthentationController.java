package demo.controller;

import demo.exception.NoRolesFoundException;
import demo.model.Role;
import demo.model.User;
import demo.model.dto.AuthenticationResponseDTO;
import demo.model.dto.LoginDTO;
import demo.model.dto.UserDTO;
import demo.security.JwtGenerator;
import demo.security.JwtValidator;
import demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/login")
public class AuthentationController {

    private JwtGenerator jwtGenerator;
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtValidator jwtValidator;

    @Autowired
    public AuthentationController(JwtGenerator jwtGenerator, AuthenticationManager authenticationManager, UserService userService, JwtValidator jwtValidator) {
        this.jwtGenerator = jwtGenerator;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtValidator = jwtValidator;
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws NoRolesFoundException {

        log.debug("register: {Username: '{}'}", userDTO.getUsername());
        List<String> roles = Arrays.asList("USER");
        userService.createNewUser(
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                roles);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);

    }
    @PostMapping(value = "/token")
    @Transactional
    public ResponseEntity<AuthenticationResponseDTO> token(@Valid @RequestBody LoginDTO loginDTO){

        log.debug("Attempting to grant token to {}", loginDTO.getEmail());

        User foundUser = userService.findByEmail(loginDTO.getEmail());
        Authentication auth = new UsernamePasswordAuthenticationToken(foundUser,loginDTO);

        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = (User) auth.getPrincipal();

        String accessToken = jwtGenerator.generate(user);

        log.debug("Successfully granted token to {}", loginDTO.getEmail());

        return ResponseEntity.ok(generateAuthenticationResponseDTO(accessToken));
    }

    @GetMapping("/principal")
    public ResponseEntity<UserDTO> currentUser() {
        Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        User user = (User)auth.getPrincipal();
        log.debug("Querying details of the current principal for user: {}", user);

        UserDTO response = new UserDTO(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null)
        );

        log.debug("Current principal resolved to {}", user.getUsername());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(@RequestBody String accessToken) {
        String refreshedAccessToken = jwtValidator.refreshAccessToken(accessToken);

        return ResponseEntity.ok(generateAuthenticationResponseDTO(refreshedAccessToken));
    }

    @GetMapping(value = "/roles")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<String>> getAllRoles() {
        return new ResponseEntity<>(userService.getAllRoles().stream().filter(r -> !r.getAuthority().equals("ADMIN") && !r.getAuthority().equals("CHANGE_PASSWORD_PRIVILEGE"))
                .map(Role::getAuthority).collect(Collectors.toList()), HttpStatus.OK);
    }

    private AuthenticationResponseDTO generateAuthenticationResponseDTO(String accessToken) {
        Long expires = Instant.now()
                .plusMillis(86400000) // 24 hours
                .toEpochMilli();

        return new AuthenticationResponseDTO(
                accessToken,
                expires);
    }
}
