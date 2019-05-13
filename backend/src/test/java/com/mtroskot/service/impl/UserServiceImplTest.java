package com.mtroskot.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.mtroskot.model.entity.auth.User;
import com.mtroskot.repository.UserRepository;
import com.mtroskot.service.UserService;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

	private UserService userService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;

	@Before
	public void setup() {
		userService = new UserServiceImpl(userRepository, passwordEncoder);
	}

	/* Saving not successful because email is invalid */
	@Test
	public void saveTest1() {
		User user = new User();
		user.setEmail("mtro.gotmail.com");

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is null", null, savedUser);
	}

	/* Saving not successful because email is invalid */
	@Test
	public void saveTest2() {
		User user = new User();
		user.setEmail(null);

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is null", null, savedUser);
	}

	/* Saving not successful because first name is invalid */
	@Test
	public void saveTest3() {
		User user = new User();
		user.setEmail("mtro@hotmail.com");
		user.setFirstName("M");

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is null", null, savedUser);
	}

	/* Saving not successful because first name is invalid */
	@Test
	public void saveTest4() {
		User user = new User();
		user.setEmail("mtro@hotmail.com");
		user.setFirstName(null);

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is null", null, savedUser);
	}

	/* Saving not successful because last name is invalid */
	@Test
	public void saveTest5() {
		User user = new User();
		user.setEmail("mtro@hotmail.com");
		user.setFirstName("Marko");
		user.setLastName("T");

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is null", null, savedUser);
	}

	/* Saving not successful because last name is invalid */
	@Test
	public void saveTest6() {
		User user = new User();
		user.setEmail("mtro@hotmail.com");
		user.setFirstName("Marko");
		user.setLastName(null);

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is null", null, savedUser);
	}

	/* Saving not successful because username is invalid */
	@Test
	public void saveTest7() {
		User user = new User();
		user.setEmail("mtro@hotmail.com");
		user.setFirstName("Marko");
		user.setLastName("Troskot");
		user.setUsername("M");

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is null", null, savedUser);
	}

	/* Saving not successful because username is invalid */
	@Test
	public void saveTest8() {
		User user = new User();
		user.setEmail("mtro@hotmail.com");
		user.setFirstName("Marko");
		user.setLastName("Troskot");
		user.setUsername(null);

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is null", null, savedUser);
	}

	/* Saving not successful because password is invalid */
	@Test
	public void saveTest9() {
		User user = new User();
		user.setEmail("mtro@hotmail.com");
		user.setFirstName("Marko");
		user.setLastName("Troskot");
		user.setUsername("MTro");
		user.setPassword("password");

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is null", null, savedUser);
	}

	/* Saving not successful because password is invalid */
	@Test
	public void saveTest10() {
		User user = new User();
		user.setEmail("mtro@hotmail.com");
		user.setFirstName("Marko");
		user.setLastName("Troskot");
		user.setUsername("MTro");
		user.setPassword(null);

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is null", null, savedUser);
	}

	/* Saving successful */
	@Test
	public void saveTest11() {
		User user = new User();
		user.setEmail("mtro@hotmail.com");
		user.setFirstName("Marko");
		user.setLastName("Troskot");
		user.setUsername("MTro");
		user.setPassword("MTro1234");

		String encodedPassword = "encodedPassword";
		Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
		Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn(encodedPassword);

		User savedUser = userService.save(user, true);

		Assert.assertEquals("Saved user is user", user, savedUser);
		Assert.assertEquals("Password is equal to encodedPassword", encodedPassword, savedUser.getPassword());
	}

	/* Saving successful */
	@Test
	public void saveTest12() {
		User user = new User();
		user.setEmail("mtro@hotmail.com");
		user.setFirstName("Marko");
		user.setLastName("Troskot");
		user.setUsername("MTro");
		user.setPassword("MTro1234");

		Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

		User savedUser = userService.save(user, false);

		Assert.assertEquals("Saved user is user", user, savedUser);
	}
}
