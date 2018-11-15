package demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.model.User;
import demo.security.JwtAuthenticationProvider;
import demo.security.JwtTokenProvider;


@RestController
@RequestMapping("/token")
public class TokenController {


	private JwtTokenProvider authenticationProvider;


	public TokenController(JwtTokenProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	@PostMapping
	public String generate(@RequestBody final User user){
return authenticationProvider.generateToken(user);

	}

}
