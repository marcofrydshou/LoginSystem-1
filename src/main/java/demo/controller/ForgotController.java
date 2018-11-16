package demo.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import demo.service.UserService;

@Controller
@RequestMapping("forgotpassword")
public class ForgotController {

	@Autowired
	private UserService userService;



}
