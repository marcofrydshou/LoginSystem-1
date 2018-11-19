package demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import demo.exception.BusinessException;
import demo.exception.NoRolesFoundException;
import demo.exception.UnauthorizedRequestException;
import demo.model.Role;
import demo.model.User;
import demo.model.UserConfigurationForm;
import demo.service.UserService;

@Slf4j
@RestController
@RequestMapping("/rest/user")
public class UserController {

	private UserService userService;

	@Autowired
	public UserController( UserService userService){ this.userService = userService; }

	@GetMapping("/all")
	@ResponseStatus(HttpStatus.OK)
	public List<User> findEnabledUsers() throws BusinessException {
		try{
			return userService.findEnabledUsers();
		}catch (Exception e){
			throw new BusinessException("Users not exists");
		}
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
	public User find(@NotBlank @PathVariable(value = "email") String email) throws BusinessException {
		try{
			return userService.findByEmail(email);
		}catch (Exception e){
			throw new BusinessException("Users with given email not exists");
		}
	}

	@PostMapping(value = "/create")
	@ResponseStatus(HttpStatus.CREATED)
	public User create(@RequestBody UserConfigurationForm newUserForm) throws NoRolesFoundException {
		log.debug("create: {Username: '{}'}", newUserForm.getUsername());
		User newUser = userService.createNewUser(
				newUserForm.getUsername(),newUserForm.getPassword(),newUserForm.getEmail(),
				newUserForm.isEnabled(),newUserForm.getRoles());
		return newUser;
	}

	@DeleteMapping(value = "delete/{user_id}")
	public boolean delete(@PathVariable("user_id") long userId){
		if(userId <= 0){
			throw new NullPointerException("User ID is invalid.");
		}
		log.debug("delete: User ID: '{}', ", userId);
		userService.deleteUser(userId);

		return true;
	}

	@PutMapping(value = "edit/{user_id}")
	public boolean edit(@PathVariable("user_id") long userId, @RequestBody UserConfigurationForm userForm) throws UnauthorizedRequestException {

//		userService.updateUser(userId);
		return true;
	}
//
//	private boolean hasAdminCreationAuthority(User user) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		List<Role> authorities = (List<Role>) auth.getAuthorities();
//		List<String> roleStrings = authorities.stream().map(Role::getAuthority).collect(Collectors.toList());
//
//		return roleStrings.contains("ADMIN");
//	}

//	private boolean verifyUserModificationAuthorization(Long requestedId) {
//		try {
//			User currentUser = SecurityUtil.getUser();
//
//			return currentUser.hasAuthority("ROLE_ADM") || requestedId.equals(currentUser.getId());
//		} catch (Exception e) {
//			log.error("User verification for a PUT/DELETE endpoint failed unexpectedly.", e);
//			return false;
//		}
//	}
}
