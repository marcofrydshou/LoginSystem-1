package demo.controller;

import static junit.framework.Assert.*;

import java.util.*;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

import demo.config.TestApplicationConfig;
import demo.exception.NoRolesFoundException;
import demo.model.Role;
import demo.model.User;
import demo.model.dto.UserDTO;
import demo.repository.RoleRepository;
import demo.repository.UserRepository;

@Slf4j
@ActiveProfiles("test")
@TestApplicationConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserController userController;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder encoder;

	private User stubUser;
	private Role role;

	@Before
	public void setUp() {
		role = roleRepository.getOne(100l);
		stubUser = new User(100, "testName", "testUsername", "test@dk.dk", "testPassword", "testAddress", true, Collections.singletonList(role));
		Authentication auth = new UsernamePasswordAuthenticationToken(stubUser, null);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	public void fail() {
		throw new RuntimeException("Test Failure method");
	}

	@Test
	@Transactional
	public void createUser() throws NoRolesFoundException {
		// create a new user
		List<String> roles = new LinkedList<>();
		roles.add("SUPERUSER");
		UserDTO newUser = new UserDTO("testUser","testPass",true, roles, "test@dk.dk");
		userController.createUser(newUser);

		// find the created usesr by username
		User createdUser = userRepository.findByUsernameAndEnabledIsTrue("testUser").get();

		// log the user email and BCrypt password
		log.info("CREATED USER'S EMAIL->: "+createdUser.getEmail());
		log.info("CREATED USER'S PASSWORD->: "+ createdUser.getPassword());

		// check the user with given username/email/enabled exists, and password mathces with 'testPass'
		assertNotNull(createdUser);
		assertEquals(createdUser.getUsername(),"testUser");
		assertEquals(createdUser.isEnabled(),true);
		assertEquals(createdUser.getEmail(),"test@dk.dk");
		assertTrue(encoder.matches("testPass",createdUser.getPassword()));
	}

	@Test
	@Transactional
	public void delteUser(){
		// save stubUser and check the user exists
		userRepository.save(stubUser);
		Optional<User> userOptional = userRepository.findByUsernameAndEnabledIsTrue("testUsername");
		assertNotNull(userOptional.get());

//		// delete the user with user id
		userController.deleteUser(userOptional.get().getId());
//
//		// find the deleted user and will throw NosuchElementException
//		Optional<User> userOptional2 = userRepository.findByIdAndEnabledIsTrue(100);
//		userOptional2.get();
	}


}