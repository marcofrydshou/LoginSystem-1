package RestController;



import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import Model.User;
import Repository.UserRepository;
import exception.ResourceNotFound;


@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping("")
	public ResponseEntity<User> createnewUser(@Valid @RequestBody User user) throws ResourceNotFound {
		User user1 = userRepository.save(user);
		return new ResponseEntity<User>(user1, HttpStatus.CREATED);
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> finduserByUsername(@PathVariable String username){
		User user = userRepository.findByUserName(username);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping("/all")
	public Iterable<User> findallUsers(){
		return userRepository.findAll();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteuser(@PathVariable String id){
		userRepository.deleteById(id);
		return new ResponseEntity<String>("User with id: "  + id+"was deleted", HttpStatus.OK);
	}


}
