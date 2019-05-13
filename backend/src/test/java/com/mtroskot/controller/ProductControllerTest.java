package com.mtroskot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.mtroskot.model.entity.product.Bread;
import com.mtroskot.model.entity.product.Product;
import com.mtroskot.security.JwtAuthenticationEntryPoint;
import com.mtroskot.security.JwtTokenProvider;
import com.mtroskot.service.ProductService;
import com.mtroskot.service.RoleService;
import com.mtroskot.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	 
	@MockBean
	private AuthenticationManager authenticationManager;
	@MockBean
	private UserService userService;
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
	@MockBean
	private ProductService productService;
	
	@Test
	@WithMockUser(username = "testuser", password = "testpass", authorities = "ROLE_USER")
	public void getAllProducts() throws Exception {
		Product p=new Bread();
		List<Product> productList = new ArrayList<>(Arrays.asList(p,p,p));
		
		Mockito.when(productService.findAll()).thenReturn(productList);

		mockMvc.perform(get("/api/product/all").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
				.andDo(print()).andExpect(status().is(200)).andExpect(content().json(objectMapper.writeValueAsString(productList)));
	}

}
