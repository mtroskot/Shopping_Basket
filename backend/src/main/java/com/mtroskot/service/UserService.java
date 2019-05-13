package com.mtroskot.service;

import java.util.Optional;

import com.mtroskot.model.entity.auth.User;

public interface UserService {

	/**
	 * Gets all users from database.
	 * 
	 * @return Iterable<User>
	 */
	Iterable<User> findAll();

	/**
	 * Finds user by id.
	 * 
	 * @param id
	 *            The id of user.
	 * @return Optional<User>
	 */
	Optional<User> findById(Long id);

	/**
	 * Finds user by username or email.
	 * 
	 * @param username
	 *            The username of user.
	 * @param email
	 *            The email of user.
	 * @return Optional<User>
	 */
	Optional<User> findByUsernameOrEmail(String username, String email);

	/**
	 * Saves user in database
	 * 
	 * @param user
	 *            The user to be saved.
	 * @param encodePassword
	 *            Indicates if should the password be encoded when saving the user
	 *            in database.Should be false if the password was previously
	 *            encoded, and true if the password is in plain text.
	 * @return User
	 */
	User save(User user, boolean encodePassword);

	/**
	 * Checks if a user exists by {@code username}
	 * 
	 * @param username
	 *            The username of user.
	 * @return Boolean
	 */
	Boolean existsByUsername(String username);

	/**
	 * Checks if a user exists by {@code email}
	 * 
	 * @param email
	 *            The email of user.
	 * @return Boolean
	 */
	Boolean existsByEmail(String email);
	

}
