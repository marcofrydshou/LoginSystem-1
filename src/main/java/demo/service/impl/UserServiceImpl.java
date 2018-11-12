package demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import demo.exception.BusinessException;
import demo.model.User;
import demo.repository.UserRepository;
import demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {



	private UserRepository userRepository;


	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder ) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public User saveUser(User user){
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setUsername(user.getUsername());

		//set confirm paasword to empty, sp postman doesntshow in plaintxt
		user.setConfirmpassword("");
		return userRepository.save(user);
	}



	@Override
	public List<User> getAllUsers() throws BusinessException {
		List<User> users = userRepository.findAll();
		return users;
	}




	@Override
	public User getUserByUsername(String username) {

		User optionalUser = userRepository.findByUsername(username);
		//User user = optionalUser.orElseThrow(() -> new BusinessException("No user for the name: " + username));
		return optionalUser;
	}

	@Override public User getOne(Long Id) {
		User user = userRepository.getOne(Id);
		return user;
	}


/*
	public User findById(Long id) {
		User user = userRepository.getOne(id);
		return user;
	}
*/
}
