package com.mtroskot.model.entity.product;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.NaturalId;

import com.mtroskot.model.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory extends BaseEntity {

	private static final long serialVersionUID = 6404227916613547216L;
	@NaturalId
	@Enumerated(EnumType.STRING)
	private Category category;

	public static enum Category {
		BREAD, MILK, MILK_PRODUCTS
	}
}
