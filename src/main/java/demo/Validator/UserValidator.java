package demo.Validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import demo.Model.User;

/*
UserValidator udpeger hvilken entity klasse der bruges til validering i metode supports
Implementerer Validator interface security spring
validate metoden bruges til at validere password og confirm password og kriterier hertil
UserValidator bruges i RestController i register medtoden */

@Component
public class UserValidator implements Validator {


	@Override public boolean supports(Class<?> aClass) {
		//supporting the user class
		return User.class.equals(aClass);
	}

	@Override public void validate(Object object, Errors errors) {
			User user = (User) object;
			if(user.getPassword().length() < 8){
				errors.rejectValue("password", "length", "must be at least 8 charachters");
			}

			if(!user.getPassword().equals(user.getConfirmpassword())){
				errors.rejectValue("password", "Match", "Passwords must match");
			}

		//confirmpassword

	}
}
