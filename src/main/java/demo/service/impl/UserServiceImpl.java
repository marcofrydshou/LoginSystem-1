package demo.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.OneToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import demo.exception.BusinessException;
import demo.model.User;
import demo.repository.UserRepository;
import demo.security.JwtTokenProvider;
import demo.service.UserService;

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

		//user.setPassword(user.getPassword());
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

		User optionalUser = userRepository.findByUsername(username);
		//User user = optionalUser.orElseThrow(() -> new BusinessException("No user for the name: " + username));
		return optionalUser;
	}


	//get user by id
	@Override public User getOne(Long Id) {
		User user = userRepository.getOne(Id);
		return user;
	}


	@Override
	public User findUserByEmail(String email){
		User user = userRepository.findByEmail(email);
		return user;
	}


//delete user entity from id
	public void deleteUser(Long id){
		Optional<User> deleteuser = userRepository.findById(id);
		if(deleteuser.isPresent()){
			userRepository.delete(deleteuser.get());
		}
	}


	// update user
	public void updateUser(Long id, User user){
		try {
			Optional<User> newuser = userRepository.findById(id);
			if(newuser.isPresent()){
				User u = newuser.get();
				u.setName(user.getName());
				u.setAddress(user.getAddress());
				u.setEmail(user.getEmail());
				u.setPassword(user.getPassword());
				u.setConfirmpassword(user.getConfirmpassword());
				userRepository.save(u);
			}

		}catch (NullPointerException ex){
			throw new NullPointerException(String.format("user with id (%s) not found ", id));
		}

	}

}
