package demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.exception.BusinessException;
import demo.model.User;
import demo.repository.UserRepository;
import demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) { this.userRepository = userRepository; }

	@Override
	public List<User> getAllUsers() throws BusinessException {
		List<User> users = userRepository.findAll();
		return users;
	}

	@Override
	public User getUserByUsername(String username) throws BusinessException {

		Optional<User> optionalUser = userRepository.findByUsername(username);
		User user = optionalUser.orElseThrow(() -> new BusinessException("No user for the name: " + username));
		return user;
	}
}
