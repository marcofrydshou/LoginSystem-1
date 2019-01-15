package demo.exposed.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import demo.data.model.dto.EditUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import demo.exposed.exception.BusinessException;
import demo.exposed.exception.NoRolesFoundException;
import demo.data.model.User;
import demo.data.model.dto.UserDTO;
import demo.business.UserService;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

	private UserService userService;

	// DI for UerService Bean
	@Autowired
	public UserController( UserService userService){ this.userService = userService; }

	@PostMapping(value = "/create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws NoRolesFoundException {

		// check the user has authentication
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if(auth.hasAuthority("ADMIN") || auth.hasAuthority("SUPERUSER") ){
			log.debug("create: {Username: '{}'}", userDTO.getUsername());
			User createdUser = userService.createNewUser(
					userDTO.getUsername(),
					userDTO.getPassword(),
					userDTO.getEmail(),
					Collections.singletonList(userDTO.getAuthority()));

			return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
		}
		else{
			log.debug("User ({}) attempted to access /create endpoint with authority ({}).", auth.getAuthorities().toString());
			throw new AuthorizationServiceException("User '%s' not authorized for this action.");
		}

	}

	@PutMapping(value = "/edit",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity editUser(@Valid @RequestBody EditUserDTO editUserDTO) throws NoRolesFoundException {

		User auth = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("Call made to edit user with id ({}) by user ({}).", editUserDTO.getId(), auth.getUsername());

		User editedUser = userService.updateUser(
				editUserDTO.getId(),
				editUserDTO.getName(),
				editUserDTO.getUsername(),
				editUserDTO.getPassword(),
				editUserDTO.getEmail(),
				editUserDTO.getAddress(),
				Collections.singletonList(editUserDTO.getAuthority()));

		log.info("User ({}) has been successfully edited." + editUserDTO.getUsername());
		return new ResponseEntity<>(editUserDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity deleteUser(@RequestBody UserDTO userDTO){
		User auth = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("Call made to delete user with id ({}) by user ({}).", userDTO.getId(), auth.getUsername());

		if (userDTO.getId() != 0) {
			userService.deleteUser(userDTO.getId());
		} else {
			throw new AuthorizationServiceException("User ID value missing from request body.");
		}

		log.info("Deleted user with ID ({}).", userDTO.getId());
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/username", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<UserDTO> findByUsername(@RequestParam("username") String username) throws BusinessException {
		UserDTO userDTO = new UserDTO();
		try{
			User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(auth.hasAuthority("ADMIN") || auth.hasAuthority("SUPERUSER") ){

				User user = userService.findByUsername(username);
				userDTO.setId(user.getId());
				userDTO.setUsername(user.getUsername());
				userDTO.setPassword(user.getPassword());
				userDTO.setEmail(user.getEmail());
				userDTO.setAuthority(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get());
			}
			else{
				log.debug("User ({}) attempted to access /username/ endpoint with authority ({}).", auth.getAuthorities().toString());
				throw new AuthorizationServiceException("User '%s' not authorized for this action.");
			}
		}catch (Exception e){
			throw new BusinessException("Users with given username not exists");
		}
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<UserDTO> findByEmail(@RequestParam("email") String email) throws BusinessException {
		UserDTO userDTO = new UserDTO();
		try{
			User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(auth.hasAuthority("ADMIN") || auth.hasAuthority("SUPERUSER") ){

				User user = userService.findByEmail(email);
				userDTO.setId(user.getId());
				userDTO.setUsername(user.getUsername());
				userDTO.setPassword(user.getPassword());
				userDTO.setEmail(user.getEmail());
				userDTO.setAuthority(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get());
			}
			else{
				log.debug("User ({}) attempted to access /email/ endpoint with authority ({}).", auth.getAuthorities().toString());
				throw new AuthorizationServiceException("User '%s' not authorized for this action.");
			}
		}catch (Exception e){
			throw new BusinessException("Users with given email not exists");
		}
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/all",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@Transactional
	public ResponseEntity<List<UserDTO>> findEnabledUsers() {

		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<UserDTO> users;

		if(auth.hasAuthority("ADMIN") || auth.hasAuthority("SUPERUSER") ){

			users = userService.findEnabledUsers()
					.stream()
					.map( u -> new UserDTO(
							u.getId(),
							u.getUsername(),
							u.getPassword(),
							u.getEmail(),
							u.getAuthoritites().stream()
											.map(GrantedAuthority::getAuthority).findFirst().orElse("No auth"))
					).collect(Collectors.toList());
		}
		else{
			log.debug("User ({}) attempted to access /create endpoint with authority ({}).", auth.getAuthorities().toString());
			throw new AuthorizationServiceException("User '%s' not authorized for this action.");
		}

		return new ResponseEntity<>(users,HttpStatus.OK);
	}

}
