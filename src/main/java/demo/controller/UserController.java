package demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
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
import demo.model.LoginForm;
import demo.security.JwtAuthenticationProvider;
import demo.security.JwtAuthenticationToken;
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


	// register new user
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


// USer login
	@PostMapping("login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginForm loginForm) throws BusinessException, IOException, ServletException {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginForm.getUsername(), loginForm.getPassword()
				)
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		User user = (User)authentication.getPrincipal();
		String token = jwtTokenProvider.generateToken(user);

		return ResponseEntity.ok(new JWTLoginSuccessResponse(true, token));

	}



// get all users
	@RequestMapping("/all")
	public List<User> getAllUsers() throws BusinessException {
		return userService.getAllUsers();
	}

	// get user from username
	@RequestMapping("getusername/{username}")
	public User getUserByName(@NotBlank @PathVariable( value = "username") String username) throws BusinessException {
		try {
			return userService.getUserByUsername(username);
		}catch (Exception ex){
			throw new BusinessException("Username not found");
		}

	}


	//get user form email
	@RequestMapping("email/{email}")
	public User findUserByEmail(@NotBlank @PathVariable(value = "email") String email) throws BusinessException {

		try {
			return userService.findUserByEmail(email);
		}catch (Exception ex){
			throw new BusinessException("no such email exist");		}
	}


	@DeleteMapping("delete/{id}")
	public boolean deleteUser(@PathVariable(value =  "id") Long id){
		try {
			if(id == null){
				throw  new NullPointerException("invalid user id");
			}
			userService.deleteUser(id);
			return true;
		}catch (Exception ex){
			throw  new NullPointerException();
		}
	}

	@PutMapping("update/{id}")
	public boolean edituser(@PathVariable(value = "id")@Valid @RequestBody User user, Long id){

		try {
			if(id == null || !id.equals(user.getId())){
				throw new NullPointerException("user id not valid or doesn't match user");
			}
				userService.updateUser(id, user);
				return true;

		}catch (Exception ex){
			throw new NullPointerException();
		}
	}


}
