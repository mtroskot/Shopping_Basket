package com.mtroskot.service;

import java.util.Optional;

import com.mtroskot.model.entity.auth.Role;
import com.mtroskot.model.entity.auth.Role.RoleType;

public interface RoleService {

	/**
	 * Finds role by type.
	 * 
	 * @param type
	 *            The type of role.
	 * @return Optional<Role>
	 */
	Optional<Role> findByType(RoleType type);

	/**
	 * Saves role.
	 * 
	 * @param role
	 *            The role to be saved.
	 * @return The saved role.
	 */
	Role save(Role role);
}
