package com.mtroskot.security;

import java.util.Collections;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mtroskot.exception.AppException;
import com.mtroskot.model.entity.auth.Role;
import com.mtroskot.model.entity.auth.Role.RoleType;
import com.mtroskot.model.entity.auth.User;
import com.mtroskot.model.request.JwtAuthenticationResponse;
import com.mtroskot.model.request.LoginRequest;
import com.mtroskot.model.request.SignUpRequest;
import com.mtroskot.service.RoleService;
import com.mtroskot.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider tokenProvider;

	/**
	 * Authenticates user by given {@code loginRequest} which contains user
	 * credentials.
	 * 
	 * @param loginRequest
	 *            The object containing user credentials needed for user
	 *            authentication.
	 * @return ResponseEntity<JwtAuthenticationResponse>
	 */
	@PostMapping("/login")
	public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		return new ResponseEntity<JwtAuthenticationResponse>(new JwtAuthenticationResponse(jwt), HttpStatus.OK);
	}

	/**
	 * Gets current user from security context.
	 * 
	 * @return The current logged user.
	 */
	@GetMapping("/currentUser")
	public UserDetails getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (UserDetails) authentication.getPrincipal();
	}

	/**
	 * Registers user by given {@code signUpRequest} which contains registration
	 * details need for registration.
	 * 
	 * @param signUpRequest
	 *            The object containing registration details.
	 * @return ResponseEntity<?>
	 */
	@PostMapping("/signup")
	public ResponseEntity<User> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (userService.existsByUsername(signUpRequest.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken!");
		}
		if (userService.existsByEmail(signUpRequest.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Address already in use!");
		}
		if (!signUpRequest.getPassword().equals(signUpRequest.getMatchingPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords don't match");
		}

		// Creating user's account
		Role userRole = roleService.findByType(RoleType.USER).orElseThrow(() -> new AppException("User Role not found."));
		Set<Role> roleSet = Collections.singleton(userRole);
		User user = signUpRequest.toUser(roleSet, passwordEncoder);
		boolean encodePassword = false; // no need to encode password, because password is encoded in signUpRequest.toUser() method
		User savedUser = userService.save(user, encodePassword);

		// savedUser is null,if the user didn't pass the validation in userService.save() method
		if (savedUser != null) {
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Creating user not successful");
	}

}