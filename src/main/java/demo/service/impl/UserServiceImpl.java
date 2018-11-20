package demo.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.OneToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import demo.exception.BusinessException;
import demo.model.User;
import demo.repository.UserRepository;
import demo.security.JwtTokenProvider;
import demo.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService {



	private UserRepository userRepository;
	private JwtTokenProvider jwtTokenProvider;


	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, BCryptPasswordEncoder bCryptPasswordEncoder ) {
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}



	public User saveUser(User user){
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setUsername(user.getUsername());
		//set confirm paasword to empty, sp postman doesntshow in plaintxt
		user.setConfirmpassword("");
		return userRepository.save(user);
	}


	//Create new user
	@Override
	public User createNewUser(User user) throws BusinessException {
		try {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setUsername(user.getUsername());
			user.setConfirmpassword("");
			return userRepository.save(user);
		}catch (Exception ex){
			throw new DataIntegrityViolationException("wrong");
		}
	}


//getallusers
	@Override
	public List<User> getAllUsers() throws BusinessException {
		List<User> users = userRepository.findAll();
		return users;
	}



//getuserbyUsername
	@Override
	public User getUserByUsername(String username) {
try {
	if(username == null){
		throw new DataIntegrityViolationException("username is null");
	}
	User optionalUser = userRepository.findByUsername(username);

	return optionalUser;

}catch (Exception ex){
	throw new DataIntegrityViolationException(String.format("no username found (%s).",username));
}
	}


	//get user by id
	@Override public User getOne(Long Id) {
		User user = userRepository.getOne(Id);
		return user;
	}


	//find user by email
	@Override
	public User findUserByEmail(String email){

		try {
			if(email == null){
				throw new DataIntegrityViolationException("No such email");
			}
			User user = userRepository.findByEmail(email);
			return user;
		}catch (Exception ex){
			throw new DataIntegrityViolationException(String.format("No user found with email (%s).", email));
		}

	}


//delete user entity from id
	public void deleteUser(Long id){
		Optional<User> deleteuser = userRepository.findById(id);
		if(deleteuser.isPresent()){
			userRepository.delete(deleteuser.get());
		}
	}


	public void updatePassword(User user, String password){
		user.setPassword(password);
		user.setTokenDate(null);
		userRepository.save(user);
	}

	// update user
	public void updateUser(Long id, User user){
		try {
			Optional<User> newuser = userRepository.findById(id);
			if(newuser.isPresent()){
					User us = newuser.get();
					us.setId(user.getId());
					us.setUsername(user.getUsername());
					us.setPassword(user.getPassword());
					us.setEmail(user.getEmail());
					us.setName(user.getName());
					us.setAddress(user.getAddress());
					us.setToken(user.getToken());
					us.setTokenDate(user.getTokenDate());
					us.setEnabled(user.isEnabled());

				userRepository.save(us);
			}

		}catch (NullPointerException ex){
			ex.getStackTrace();
			ex.toString();
			throw new NullPointerException(String.format("user with id (%s) not found ", id));
		}

	}



}
