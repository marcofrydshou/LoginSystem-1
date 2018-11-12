package demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.Model.User;
import demo.Repository.UserRepository;


@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(userName);
		if(user == null)new UsernameNotFoundException("USer not found");
		return user;
	}



	@Transactional
	public User loadUserById(Long Id){
		User user = userRepository.getById(Id);
		if(user==null)new UsernameNotFoundException("User not found");
		return user;
	}



}
