package demo.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
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
		try{
			return userService.findEnabledUsers();
		}catch (Exception e){
			throw new BusinessException("Users not exists");
		}
	}

	@GetMapping("/user/name/{username}")
	public User findByUsername(@NotBlank @PathVariable(value = "username") String username) throws BusinessException {
		try{
			return userService.findByUsername(username);
		}catch (Exception e){
			throw new BusinessException("Users with given username not exists");
		}
	}

	@GetMapping("/user/email/{email}")
	public User find(@NotBlank @PathVariable(value = "email") String email) throws BusinessException {
		try{
			return userService.findByEmail(email);
		}catch (Exception e){
			throw new BusinessException("Users with given email not exists");
		}
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
