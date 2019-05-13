package com.mtroskot.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mtroskot.model.entity.auth.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserServiceImpl userService;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		// User can login either by username or email
		Optional<User> user = userService.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
		if (user.isPresent()) {
			return user.get();
		}
		throw new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail);
	}
}
