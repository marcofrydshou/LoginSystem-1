package demo.controller;

import org.springframework.web.bind.annotation.*;

import demo.config.security.JwtGenerator;
import demo.model.User;

@RestController
@RequestMapping("/token")
public class TokenController {

	private JwtGenerator jwtGenerator;

	public TokenController(JwtGenerator jwtGenerator) {
		this.jwtGenerator = jwtGenerator;
	}

	@PostMapping
	public String generate(@RequestBody final User user){

		return jwtGenerator.generate(user);
	}
}
