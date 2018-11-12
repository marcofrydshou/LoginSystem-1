package demo.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import demo.Payload.JWTLoginSuccessResponse;
import demo.Payload.LoginRequest;
import demo.Security.JwtTokenProvider;
import demo.Validator.UserValidator;
import demo.exception.BusinessException;
import demo.model.User;
import demo.service.UserService;
import demo.service.impl.UserServiceImpl;

import static demo.Security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api")
public class UserController {

	private UserService userService;

	private UserServiceImpl userServiceImpl;

	private UserValidator userValidator;

	private JwtTokenProvider jwtTokenProvider;

	private AuthenticationManager authenticationManager;

	@Autowired
	public UserController( UserService userService, UserServiceImpl userServiceImpl, UserValidator userValidator, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager){
		this.userService = userService;
		this.userServiceImpl = userServiceImpl;
		this.userValidator = userValidator;
		this.jwtTokenProvider = jwtTokenProvider;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/user/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
		//TODO: validate password
		userValidator.validate(user, result);

		User user1 = userServiceImpl.saveUser(user);
		return new ResponseEntity<User>(user1, HttpStatus.CREATED);
	}


	@PostMapping("/user/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUserName(),
						loginRequest.getPassword()
				)
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
	}




	@RequestMapping("/getAllUsers")
	public List<User> getAllUsers() throws BusinessException {
		return userService.getAllUsers();
	}

	@RequestMapping("/getUserByName")
	public User getUserByName(@NotBlank @RequestParam(value = "name") String name) throws BusinessException {
		return userService.getUserByUsername(name);
	}


}
