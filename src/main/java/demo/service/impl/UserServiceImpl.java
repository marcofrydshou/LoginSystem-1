package demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import demo.exception.DataIntegrityViolationException;
import demo.exception.NoRolesFoundException;
import demo.model.Role;
import demo.model.User;
import demo.repository.RoleRepository;
import demo.repository.UserRepository;
import demo.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder encoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
	}

	@Override
	public List<User> findEnabledUsers() {
		log.info("Find all enabled users");
		return userRepository.findUserByEnabledIsTrue();
	}

	@Override
	public User findUserById(long userId) {
		User user = new User();
		try{
			Optional<User> optionalUser = userRepository.findByIdAndEnabledIsTrue(userId);
			if(optionalUser.isPresent()){
				user = optionalUser.get();
			}
		}
		catch (DataIntegrityViolationException e){
			log.debug("Error accourced from findUserById {userId} " + userId);
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User createNewUser(String username, String password, String email, List<String> roles) throws NoRolesFoundException,DataIntegrityViolationException {
		try{
			List<Role> authorities = roleRepository.findByAuthorityIn(roles);

			if (authorities.isEmpty()) {
				throw new NoRolesFoundException("No roles found for given role names.");
			}

			password = encoder.encode(password);
			User newUser = new User(username, password, email, true, authorities);
			return userRepository.save(newUser);
		}
		catch (DataIntegrityViolationException e){
			throw new DataIntegrityViolationException(String.format("Duplicate email.") + e);
		}
	}

	@Override
	public void updateUser(long userId, User userInfo) throws DataIntegrityViolationException {
		try{
			Optional<User> optionalFetchedUser = userRepository.findById(userId);
			if(optionalFetchedUser.isPresent()){
				User fetchedUser = optionalFetchedUser.get();
				fetchedUser.setName(userInfo.getName());
				fetchedUser.setAddress(userInfo.getAddress());
				fetchedUser.setEmail(userInfo.getEmail());
				fetchedUser.setPassword(userInfo.getPassword());
				fetchedUser.setEnabled(userInfo.isEnabled());
				userRepository.save(fetchedUser);
				if (userId <= 0) {
					throw new NullPointerException("User ID is invalid.");
				}

			}
		}
		catch (NullPointerException e){
			throw new NullPointerException(String.format("User not found with given id (%s)", userId));
		}
	}

	public void deleteUser(long userId) throws DataIntegrityViolationException {
		Optional<User> userToDelete = userRepository.findById(userId);
		userToDelete.orElseThrow( ()-> new UsernameNotFoundException("Provided user not found."));
		userRepository.delete(userToDelete.get());
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

		userRepository.save(user);
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}


}
