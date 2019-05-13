package com.mtroskot.model.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.mtroskot.model.entity.auth.Role;
import com.mtroskot.model.entity.auth.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpRequest {

	@NotBlank
	@Size(min = 2, max = 20)
	private String firstName;

	@NotBlank
	@Size(min = 2, max = 20)
	private String lastName;

	@NotBlank
	@Size(min = 3, max = 15)
	private String username;

	@NotBlank
	@Size(max = 40)
	@Email
	private String email;

	@NotBlank
	@Size(min = 6, max = 20)
	private String password;

	@NotBlank
	@Size(min = 6, max = 20)
	private String matchingPassword;
	
	public User toUser(Set<Role> roles,PasswordEncoder passwordEncoder) {
		return new User(username,email, passwordEncoder.encode(password),firstName,lastName,roles);
	}
}
