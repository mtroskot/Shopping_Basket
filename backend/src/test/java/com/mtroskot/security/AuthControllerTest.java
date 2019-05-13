package com.mtroskot.security;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtroskot.model.entity.auth.Role;
import com.mtroskot.model.entity.auth.Role.RoleType;
import com.mtroskot.model.entity.auth.User;
import com.mtroskot.model.request.SignUpRequest;
import com.mtroskot.service.RoleService;
import com.mtroskot.service.impl.UserServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	

	@MockBean
	private AuthenticationManager authenticationManager;
	@MockBean
	private UserServiceImpl userService;
	@MockBean
	private RoleService roleService;
	@MockBean
	private PasswordEncoder passwordEncoder;
	@MockBean
	private JwtTokenProvider tokenProvider;
	@MockBean
	private UserDetailsService userDetailsService;
	@MockBean
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	private String usernameTaken = "Username is already taken!";
	private String emailTaken = "Email Address already in use!";
	private String passwordsNoMatch = "Passwords don't match";
	private String notSuccessful = "Creating user not successful";

	
	/*
	 * Should return HTTP code 400, because request body is missing
	 */
	@Test
	public void registerUserTest1() throws Exception {

		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")).andDo(print()).andExpect(status().is(400));

	}

	/*
	 * Should return HTTP code 400, because username is already taken
	 */
	@Test
	public void registerUserTest2() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("Marko", "Troskot", "MTro", "marko@gotmail.com", "pass123", "pass123");

		Mockito.when(userService.existsByUsername(ArgumentMatchers.anyString())).thenReturn(true);

		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpRequest)).characterEncoding("UTF-8"))
				.andDo(print()).andExpect(status().is(400)).andExpect(status().reason(usernameTaken));
	}

	/*
	 * Should return HTTP code 400, because email is already taken
	 */
	@Test
	public void registerUserTest3() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("Marko", "Troskot", "MTro", "marko@gotmail.com", "pass123", "pass123");

		Mockito.when(userService.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
		Mockito.when(userService.existsByEmail(ArgumentMatchers.anyString())).thenReturn(true);

		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpRequest)).characterEncoding("UTF-8"))
				.andDo(print()).andExpect(status().is(400)).andExpect(status().reason(emailTaken));
	}

	/*
	 * Should return HTTP code 400, because passwords don't match
	 */
	@Test
	public void registerUserTest4() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("Marko", "Troskot", "MTro", "marko@gotmail.com", "pass123", "pass1234");

		Mockito.when(userService.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
		Mockito.when(userService.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);

		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpRequest)).characterEncoding("UTF-8"))
				.andDo(print()).andExpect(status().is(400)).andExpect(status().reason(passwordsNoMatch));
	}

	/*
	 * Should return HTTP code 500, because role not found
	 */
	@Test
	public void registerUserTest5() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("Marko", "Troskot", "MTro", "marko@gotmail.com", "pass123", "pass123");

		Mockito.when(userService.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
		Mockito.when(userService.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);
		Mockito.when(roleService.findByType(ArgumentMatchers.any(RoleType.class))).thenReturn(Optional.ofNullable(null));

		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpRequest)).characterEncoding("UTF-8"))
				.andDo(print()).andExpect(status().is(500));
	}

	/*
	 * Should return HTTP code 400, because validation not passed in userService
	 * save method
	 */
	@Test
	public void registerUserTest6() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("Marko", "Troskot", "MTro", "marko@gotmail.com", "pass123", "pass123");

		Mockito.when(userService.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
		Mockito.when(userService.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);
		Role role = new Role();
		role.setType(RoleType.USER);
		Mockito.when(roleService.findByType(ArgumentMatchers.any(RoleType.class))).thenReturn(Optional.of(role));
		Mockito.when(userService.save(ArgumentMatchers.any(User.class), ArgumentMatchers.anyBoolean())).thenReturn(null);

		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpRequest)).characterEncoding("UTF-8"))
				.andDo(print()).andExpect(status().is(400)).andExpect(status().reason(notSuccessful));
	}

	/*
	 * Should return HTTP code 201, user registered
	 */
	@Test
	public void registerUserTest7() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("Marko", "Troskot", "MTro", "marko@gotmail.com", "pass123", "pass123");

		Mockito.when(userService.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
		Mockito.when(userService.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);
		Role role = new Role();
		role.setType(RoleType.USER);
		Mockito.when(roleService.findByType(ArgumentMatchers.any(RoleType.class))).thenReturn(Optional.of(role));
		User user = new User();
		Mockito.when(userService.save(ArgumentMatchers.any(User.class), ArgumentMatchers.anyBoolean())).thenReturn(user);

		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpRequest)).characterEncoding("UTF-8"))
				.andDo(print()).andExpect(status().is(201)).andExpect(content().json(objectMapper.writeValueAsString(user)));
	}

	/* Should return 200 and the current user */
	@Test
	@WithMockUser(username = "testuser", password = "testpass", authorities = "ROLE_USER")
	public void getCurrentUserTest2() throws Exception {
		mockMvc.perform(get("/api/auth/currentUser")).andDo(print()).andExpect(status().is(200)).andExpect(content().string(containsString("testuser")));
	}
}
