package demo.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.exception.BusinessException;
import demo.model.User;
import demo.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	private UserService userService;

	@Autowired
	public UserController( UserService userService){ this.userService = userService; }

	@RequestMapping("/getAllUsers")
	public List<User> getAllUsers() throws BusinessException {
		return userService.getAllUsers();
	}

	@RequestMapping("/getUserByName")
	public User getUserByName(@NotBlank @RequestParam(value = "name") String name) throws BusinessException {
		return userService.getUserByUsername(name);
	}

	
}
