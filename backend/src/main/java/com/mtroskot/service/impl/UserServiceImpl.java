package com.mtroskot.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mtroskot.model.entity.auth.User;
import com.mtroskot.repository.UserRepository;
import com.mtroskot.service.UserService;
import com.mtroskot.utils.validation.ValidationUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> findByUsernameOrEmail(String username, String email) {
		return userRepository.findByUsernameOrEmail(username, email);
	}

	@Override
	public User save(User user, boolean encodePassword) {
		boolean isValid = true;
		if (!ValidationUtils.validate(user.getEmail(), ValidationUtils.PATTERN_EMAIL)) {
			isValid = false;
		} else if (!ValidationUtils.validate(user.getFirstName(), ValidationUtils.PATTERN_FIRST_NAME)) {
			isValid = false;
		} else if (!ValidationUtils.validate(user.getLastName(), ValidationUtils.PATTERN_LAST_NAME)) {
			isValid = false;
		} else if (!ValidationUtils.validate(user.getUsername(), ValidationUtils.PATTERN_USERNAME)) {
			isValid = false;
		} else if (!ValidationUtils.validate(user.getPassword(), ValidationUtils.PATTERN_PASSWORD)) {
			isValid = false;
		}
		if (isValid) {
			if (encodePassword) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
			}
			return userRepository.save(user);
		} else {
			log.debug("Saving user not successfull for user {}", user);
			return null;
		}
	}

	@Override
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsernameIgnoreCase(username);
	}

	@Override
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmailIgnoreCase(email);
	}

}
