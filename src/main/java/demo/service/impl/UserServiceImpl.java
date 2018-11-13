package demo.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import demo.exception.DataIntegrityViolationException;
import demo.model.User;
import demo.repository.UserRepository;
import demo.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) { this.userRepository = userRepository; }

	@Override
	public List<User> findEnabledUsers() {
		log.info("Find all enabled users");
		return userRepository.findUserByEnabledIsTrue();
	}

	@Override public User findUserById(Long userId) {
		return null;
	}

	@Override
	public
	void createNewUser(User newUser) throws DataIntegrityViolationException {
		try{
			newUser.hashPassword();
			userRepository.save(newUser);
			log.info("Create new user: "+ newUser);
		}
		catch (DataIntegrityViolationException e){
			throw new DataIntegrityViolationException(String.format("Duplicate email.") + e);
		}
	}

	@Override
	public void updateUser(Long userId, User userInfo) throws DataIntegrityViolationException {
		try{
			Optional<User> optionalFetchedUser = userRepository.findById(userId);
			if(optionalFetchedUser.isPresent()){
				User fetchedUser = optionalFetchedUser.get();
				fetchedUser.setName(userInfo.getName());
				fetchedUser.setAddress(userInfo.getAddress());
				fetchedUser.setEmail(userInfo.getEmail());
				fetchedUser.setPassword(userInfo.getPassword());
				fetchedUser.setEnabled(userInfo.isEnabled());
//				fetchedUser.hashPassword();
				userRepository.save(fetchedUser);
			}
		}
		catch (NullPointerException e){
			throw new NullPointerException(String.format("User not found with given id (%s)", userId));
		}
	}

	@Override
	@Transactional
	public void deleteUser(Long userId) throws DataIntegrityViolationException {
		Optional<User> optionalUser = userRepository.findById(userId);
		if(optionalUser.isPresent()){
			userRepository.delete(optionalUser.get());
		}
	}

	@Override
	@Cacheable(value = "users")
	public User findByUsername(String username) throws DataIntegrityViolationException {
		log.debug("Find user with given username: " + username);
		try {
			if(username == null){
				throw new DataIntegrityViolationException("Username is null");
			}
			log.info("Find user with given username: " + username);
			Optional<User> optionalUser = userRepository.findByUsernameAndEnabledIsTrue(username);
			return optionalUser.orElseThrow(() -> new DataIntegrityViolationException("No user for this username: " + username));
		}
		catch (Exception e){
			log.debug(e.getMessage(),e);
			throw new DataIntegrityViolationException(username);
		}
	}

	@Override
	public User findByEmail(String email) throws DataIntegrityViolationException  {
		log.debug("Find user with given email: " + email);
		try {
			if (email == null) {
				throw new DataIntegrityViolationException("Email is null");
			}
			log.info("Find user with given email: " + email);
			Optional<User> optionalUser = userRepository.findByEmailAndEnabledIsTrue(email);
			return optionalUser.orElseThrow(() -> new DataIntegrityViolationException("No user for this email: " + email));
		}
		catch (Exception e){
			log.debug(e.getMessage(),e);
			throw new DataIntegrityViolationException(String.format("No user found with given email address (%s).",email));
		}
	}

	@Override
	public void updatePassword(User user, String password) {
		user.setPassword(password);
//		user.hashPassword();

		user.setToken(null);
		user.setTokenDate(null);

		userRepository.save(user);
	}
}
