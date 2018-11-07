package controller;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.User;
import repository.UserRepository;

@RestController
public class UserController {

	private UserRepository userRepository;

	@Autowired
	public UserController( UserRepository userRepository){ this.userRepository = userRepository; }

	@RequestMapping("/getAllUsers")
	public List<User> getAllUsers(){ return (List<User>) userRepository.findAll(); }

	@RequestMapping("/getUserById")
	public Optional<User> getCustomerById(@NotBlank @RequestParam(value = "id") Long id) {
		return userRepository.findById(id);
	}

	@RequestMapping("/getCustomerByName")
	public List<User> getCustomerByFistName(@RequestParam(value = "name", defaultValue = "Jack") String name) {
		return (List<User>) userRepository.findByName(name);
	}
}
