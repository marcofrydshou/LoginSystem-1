package demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import demo.security.JwtAuthenticationTokenFilter;
import demo.security.JwtTokenProvider;
import demo.Validator.UserValidator;
import demo.exception.BusinessException;
import demo.model.User;
import demo.service.UserService;
import demo.service.impl.UserServiceImpl;


@RestController
@RequestMapping("/rest")
public class UserController {

	private UserService userService;

	private UserServiceImpl userServiceImpl;

	private UserValidator userValidator;

	private JwtTokenProvider jwtTokenProvider;

	private AuthenticationManager authenticationManager;

	private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

	@Autowired
	public UserController( UserService userService, UserServiceImpl userServiceImpl, UserValidator userValidator, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager){
		this.userService = userService;
		this.userServiceImpl = userServiceImpl;
		this.userValidator = userValidator;
		this.jwtTokenProvider = jwtTokenProvider;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
		//TODO: validate password

		User user1 = userServiceImpl.saveUser(user);
		return new ResponseEntity<User>(user1, HttpStatus.CREATED);
	}


	@PostMapping("token")
	public String generate(@RequestBody final User user){
		return jwtTokenProvider.generateToken(user);

	}


/*

	@PostMapping("/user/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody HttpServletRequest req, HttpServletResponse res){




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

*/


	@RequestMapping("/all")
	public List<User> getAllUsers() throws BusinessException {
		return userService.getAllUsers();
	}

	@RequestMapping("/getusername")
	public User getUserByName(@NotBlank @RequestParam(value = "name") String name) throws BusinessException {
		return userService.getUserByUsername(name);
	}


}
