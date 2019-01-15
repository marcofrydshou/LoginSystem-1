package demo.business.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import demo.exposed.exception.DataIntegrityViolationException;
import demo.exposed.exception.NoRolesFoundException;
import demo.data.model.Role;
import demo.data.model.User;
import demo.data.repository.RoleRepository;
import demo.data.repository.UserRepository;
import demo.business.UserService;

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
	public User loginUser(String emailDTO, String passwordDTO) {

		Optional<User> foundUserOptional = userRepository.findByEmailIgnoreCase(emailDTO);

		User user = new User();

		if(foundUserOptional.isPresent()){
			user = foundUserOptional.get();
			if(encoder.matches(passwordDTO,user.getPassword())){
				return user;
			}
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
			log.info("new user with encode password->" + newUser);
			return userRepository.save(newUser);
		}
		catch (DataIntegrityViolationException e){
			throw new DataIntegrityViolationException(String.format("Duplicate email.") + e);
		}
	}

	@Override
	public User updateUser(long userId, String newName, String newUsername, String newPassword, String newEmail,String newAddress, List<String> newRoles) throws NoRolesFoundException {
			// find the exists user by given user id
			Optional<User> userOptional = userRepository.findById(userId);

			User user = userOptional.orElseThrow( () -> new UsernameNotFoundException("Provided user ID not found."));

			if(newName != null){
				user.setName(newName);
			}
			if(newUsername != null){
				user.setUsername(newUsername);
			}
			if(newPassword != null){
				user.setPassword(encoder.encode(newPassword));
			}
			if(newEmail != null){
				user.setEmail(newEmail);
			}
			if(newAddress != null){
				user.setAddress(newAddress);
			}
			if(newRoles != null){
				List<Role> authorityEntities = roleRepository.findByAuthorityIn(newRoles);
				if(authorityEntities.isEmpty()){
					throw new NoRolesFoundException("No roles found for given role names.");
				}
				user.setAuthoritites(authorityEntities);
			}
			userRepository.save(user);

		return user;
	}

	@Override
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

		log.debug("Find user by given email: " + email);

		if (email == null) {
			throw new DataIntegrityViolationException("Email is null");
		}

		log.info("Find user with given email: " + email);

		Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);

		return optionalUser.orElseThrow(() -> new DataIntegrityViolationException("No user for this email address (%s). " + email));

	}

	@Override
	public void updatePassword(User user, String password) {
		user.setPassword(password);

		userRepository.save(user);
	}

	@Override
	public List<User> findEnabledUsers() throws NoSuchElementException {
		List<User> users;

		try{
			log.info("Find all enabled users");
			users = userRepository.findUsersByEnabledIsTrue();
		}
		catch (NoSuchElementException e){
			throw new NoSuchElementException("No value present");
		}
		return users;
	}

	@Override
	public void save(User user) {
		try{
			log.info("Save the given user");
			userRepository.save(user);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

}
