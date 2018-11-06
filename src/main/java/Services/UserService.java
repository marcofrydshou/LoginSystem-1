package Services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Model.User;
import Repository.UserRepository;

@Service
public class UserService {


	@Autowired
	private UserRepository userRepository;

	public User saveUser(User user){

		return userRepository.save(user);
	}

	public User findByUserName(String userName){
		return userRepository.findByUserName(userName);
	}

	public Iterable<User> findall(){
		return userRepository.findAll();
	}

	public void deleteUserbyid(String id){
		userRepository.deleteById(id);

	}


}
