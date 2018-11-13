package demo.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import demo.exception.BusinessException;
import demo.model.User;
import demo.service.UserService;

@Slf4j
@RestController
@RequestMapping("/rest")
public class UserController {

	private UserService userService;

	@Autowired
	public UserController( UserService userService){ this.userService = userService; }

	@GetMapping("/")
	public String hello(){
		return "hello world";
	}

	@GetMapping("/users")
	public List<User> findEnabledUsers() throws BusinessException {
		return userService.findEnabledUsers();
	}

	@GetMapping("/user/name/{username}")
	public User findByUsername(@NotBlank @PathVariable(value = "username") String username) throws BusinessException {
		return userService.findByUsername(username);
	}

	@GetMapping("/user/email/{email}")
	public User find(@NotBlank @PathVariable(value = "email") String email) throws BusinessException {
		return userService.findByEmail(email);
	}
/*
	@PostMapping(value = "/user/create")
	public boolean create(@RequestBody UserConfigurationForm newUserForm) throws BusinessException {
		log.debug("create: (Username: '{}'),", newUserForm.getUsername());
		User newUser = new User(newUserForm.getUsername(), newUserForm.getPassword(), newUserForm.isEnabled(),newUserForm.getEmail());

		userService.createNewUser(newUser);

		return true;
	}*/
}
