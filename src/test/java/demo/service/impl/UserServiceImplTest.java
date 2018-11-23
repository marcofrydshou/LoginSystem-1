package demo.service.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

import demo.model.User;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceImplTest {

	@Autowired
	private UserServiceImpl userService;

	@Test
	public void fail() {
		throw new RuntimeException("Test Failure method");
	}

	@Test
	public void findEnabledUsers(){
		try{
			List<User> users = userService.findEnabledUsers();

		}
		catch (Exception e){
			e.printStackTrace();
		}

	}
}
