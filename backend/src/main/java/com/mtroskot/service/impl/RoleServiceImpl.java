package com.mtroskot.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mtroskot.model.entity.auth.Role;
import com.mtroskot.model.entity.auth.Role.RoleType;
import com.mtroskot.repository.RoleRepository;
import com.mtroskot.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	@Override
	public Optional<Role> findByType(RoleType type) {
		return roleRepository.findByType(type);
	}

	@Override
	public Role save(Role role) {
		return roleRepository.save(role);
	}

}
