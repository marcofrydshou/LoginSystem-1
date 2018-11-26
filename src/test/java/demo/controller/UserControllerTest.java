package demo.controller;

import demo.config.H2DatabaseInitializer;
import demo.config.TestApplicationConfig;
import demo.exception.NoRolesFoundException;
import demo.model.Role;
import demo.model.User;
import demo.model.dto.UserDTO;
import demo.repository.RoleRepository;
import demo.repository.UserRepository;
import demo.service.UserService;
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

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;


@Slf4j
@ActiveProfiles("test")
@TestApplicationConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

    private User fooUser;


	@Before
	public void setUp() {
	    // create 5 users in h2 database from the script
        H2DatabaseInitializer.truncate().thenRunSQLScriptFromResources("test-data");
        // create a new authenticated user and save the suer into securitycontextholder
        Role role = roleRepository.getOne(100l);
        fooUser = new User("fooUsername", "fooPass", "foo@dk.dk",true,Collections.singletonList(role));
        Authentication auth = new UsernamePasswordAuthenticationToken(fooUser, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
	}


	@Test
	public void fail() {
		throw new RuntimeException("Test Failure method");
	}


	@Test
	@Transactional
	public void createUser() throws NoRolesFoundException {

	    // create new user
        // HINT! change @GeneratedValue(strategy = GenerationType.IDENTITY) to @GeneratedValue
		UserDTO newUser = new UserDTO("testUser","testPass","test@dk.dk","SUPERUSER" );
		userController.createUser(newUser);

		// find the created usesr by username
		User createdUser = userRepository.findByUsernameAndEnabledIsTrue("testUser").get();


		// debugging
		log.info("CREATED USER'S EMAIL->: "+createdUser.getEmail());
		log.info("CREATED USER'S PASSWORD->: "+ createdUser.getPassword());

		// check the user with given username/email/enabled exists, and password mathces with 'testPass'
		assertNotNull(createdUser);
		assertEquals(createdUser.getUsername(),"testUser");
		assertEquals(createdUser.isEnabled(),true);
		assertEquals(createdUser.getEmail(),"test@dk.dk");
		assertTrue(encoder.matches("testPass",createdUser.getPassword()));
	}


	@Test(expected = NoSuchElementException.class)
	@Transactional
	public void delteUser(){
		// save stubUser and check the user exists
		userRepository.save(fooUser);
		Optional<User> userOptional = userRepository.findByUsernameAndEnabledIsTrue("testUsername");
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userOptional.get().getId());
		assertNotNull(userOptional.get());

		// delete the user with user id
		userController.deleteUser(userDTO);

		// find the deleted user and will throw NosuchElementException
		Optional<User> userOptional2 = userRepository.findByIdAndEnabledIsTrue(100);
		userOptional2.get();
	}

}
