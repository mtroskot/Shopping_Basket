package com.mtroskot.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mtroskot.model.entity.auth.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

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
	 * Checks if a user exists by {@code username} case insensitive.
	 * 
	 * @param username
	 *            The username of user.
	 * @return Boolean
	 */
	Boolean existsByUsernameIgnoreCase(String username);

	/**
	 * Checks if a user exists by {@code email} case insensitive.
	 * 
	 * @param email
	 *            The email of user.
	 * @return Boolean
	 */
	Boolean existsByEmailIgnoreCase(String email);

}
