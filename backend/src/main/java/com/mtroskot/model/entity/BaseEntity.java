package com.mtroskot.model.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false, of = { "id", "version" })
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -3996879536749776888L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/*
	 * Helpful when checking if a change has happened while trying to persist the
	 * object
	 */
	@Version
	private Long version;

	public BaseEntity(Long id, Long version) {
		this.id = id;
		this.version = version;
	}
	
	
}
