package demo.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import demo.Payload.JWTLoginSuccessResponse;
import demo.Payload.LoginRequest;
import demo.exception.InvalidLoginResponse;
import demo.security.JwtAuthenticationProvider;
import demo.security.JwtAuthenticationTokenFilter;
import demo.security.JwtTokenProvider;
import demo.Validator.UserValidator;
import demo.exception.BusinessException;
import demo.model.User;
import demo.service.UserService;
import demo.service.impl.UserServiceImpl;


@Slf4j
@RestController
@RequestMapping("/rest")
public class UserController {

	private UserService userService;

	private UserServiceImpl userServiceImpl;

	private UserValidator userValidator;

	private JwtTokenProvider jwtTokenProvider;

	private JwtAuthenticationProvider jwtAuthenticationProvider;

	private AuthenticationManager authenticationManager;

	private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

	@Autowired
	public UserController( UserService userService, UserServiceImpl userServiceImpl, UserValidator userValidator, JwtTokenProvider jwtTokenProvider, JwtAuthenticationProvider jwtAuthenticationProvider, AuthenticationManager authenticationManager){
		this.userService = userService;
		this.userServiceImpl = userServiceImpl;
		this.userValidator = userValidator;
		this.jwtTokenProvider = jwtTokenProvider;
		this.jwtAuthenticationProvider = jwtAuthenticationProvider;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result, Errors errors) throws BusinessException {

		if(user.getPassword().length() < 8){
			errors.rejectValue("password", "length", "must be at least 8 charachters");
		}

		if(!user.getPassword().equals(user.getConfirmpassword())){
			errors.rejectValue("password", "Match", "Passwords must match");
		}

		String gen = jwtTokenProvider.generateToken(user);
		log.debug("JWT ", gen);
		User user1 = userServiceImpl.createNewUser(user);
		return new ResponseEntity<User>(user1, HttpStatus.CREATED);
	}



	@PostMapping("login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody User user) throws BusinessException, IOException, ServletException {

		User user1 = userService.getUserByUsername(user.getUsername());
		if(user1== null){
			throw new NullPointerException("user does not exist");
		}

		String jwt = jwtTokenProvider.generateToken(user1);
		jwtTokenProvider.validateToken(jwt);

		return new ResponseEntity<User>(user1, HttpStatus.OK);

		//Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));


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
	public User getUserByName(@NotBlank @PathVariable( value = "name") String username) throws BusinessException {
		try {
			return userService.getUserByUsername(username);
		}catch (Exception ex){
			throw new BusinessException("Username not found");
		}

	}


}
