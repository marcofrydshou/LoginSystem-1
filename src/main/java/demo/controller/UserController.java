package demo.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;

import demo.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import demo.exception.BusinessException;
import demo.exception.NoRolesFoundException;
import demo.exception.UnauthorizedRequestException;
import demo.model.User;
import demo.model.dto.UserDTO;
import demo.service.UserService;

@Slf4j
@RestController
@RequestMapping("/rest/user")
public class UserController {

	private UserService userService;

	@Autowired
	public UserController( UserService userService){ this.userService = userService; }

	@GetMapping(value = "/all",produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<UserDTO>> findEnabledUsers() {

			List<UserDTO> users = userService.findEnabledUsers()
					.stream()
					.map( u -> new UserDTO(
							u.getId(),
							u.getUsername(),
							u.getPassword(),
							u.getEmail(),
							u.getAuthoritites().stream()
								.map(i-> i.getAuthority()).findFirst().get())
					).collect(Collectors.toList());

			return new ResponseEntity<>(users,HttpStatus.OK);
	}

	@GetMapping("name/{username}")
	public User findByUsername(@NotBlank @PathVariable(value = "username") String username) throws BusinessException {
		try{
			return userService.findByUsername(username);
		}catch (Exception e){
			throw new BusinessException("Users with given username not exists");
		}
	}

	@GetMapping("/email/{email}")
	public User findByEmail(@NotBlank @PathVariable(value = "email") String email) throws BusinessException {
		try{
			return userService.findByEmail(email);
		}catch (Exception e){
			throw new BusinessException("Users with given email not exists");
		}
	}

	@PostMapping(value = "/create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws NoRolesFoundException {

		// check the user has authentication
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> authorities = (List<Role>)auth.getAuthorities();
        List<String> roles = authorities.stream().map(Role::getAuthority).collect(Collectors.toList());

		if(roles.contains("ADMIN") || roles.contains("SUPERUSER") ){
			log.debug("create: {Username: '{}'}", userDTO.getUsername());
			User createdUser = userService.createNewUser(
					userDTO.getUsername(),
					userDTO.getPassword(),
					userDTO.getEmail(),
					Collections.singletonList(userDTO.getAuthority()));
			log.info("CREATED USER->" + createdUser);

			return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
		}
		else{
            log.debug("User ({}) attempted to access /create endpoint with authority ({}).", auth.getAuthorities().toString());
            throw new AuthorizationServiceException("User '%s' not authorized for this action.");
        }

	}

	@DeleteMapping(value = "delete/{user_id}")
	public boolean deleteUser(@PathVariable("user_id") long userId){
		if(userId <= 0){
			throw new NullPointerException("User ID is invalid.");
		}
		log.debug("delete: User ID: '{}', ", userId);
		userService.deleteUser(userId);

		return true;
	}

	@PutMapping(value = "edit/{user_id}")
	public boolean editUser(@PathVariable("user_id") long userId, @RequestBody UserDTO userForm) throws UnauthorizedRequestException {

		return true;
	}


	private boolean hasAdminCreationAuthority(UserDTO userDTO) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Role> authorities = (List<Role>) auth.getAuthorities();
		List<String> roleStrings = authorities.stream().map(Role::getAuthority).collect(Collectors.toList());

		return roleStrings.contains("ADMIN");
	}

}
