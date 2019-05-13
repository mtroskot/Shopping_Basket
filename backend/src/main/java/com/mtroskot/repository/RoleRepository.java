package com.mtroskot.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mtroskot.model.entity.auth.Role;
import com.mtroskot.model.entity.auth.Role.RoleType;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	/**
	 * Finds role by type.
	 * 
	 * @param type
	 *            The type of role.
	 * @return Optional<Role>
	 */
	Optional<Role> findByType(RoleType type);

}
