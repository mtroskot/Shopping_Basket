package com.mtroskot.model.entity.product;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.mtroskot.model.entity.BaseEntity;
import com.mtroskot.utils.validation.ValidationUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = { "PK_DELIMITER" })
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Product extends BaseEntity {

	private static final long serialVersionUID = -2727940749826285043L;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@Transient
	private final String PK_DELIMITER = "_";

	private String name;
	private double pricePerUnit;
	private double discountPercentage;
	@Cascade(CascadeType.ALL)
	@OneToOne
	private ProductCategory category;

	public Product(String name, double pricePerUnit, double discountPercentage, ProductCategory category) {
		setName(name);
		setPricePerUnit(pricePerUnit);
		setDiscountPercentage(discountPercentage);
		setProductCategory(category);
	}

	public Product(Product newProduct) {
		super(newProduct.getId(), newProduct.getVersion());
		setName(newProduct.getName());
		setPricePerUnit(newProduct.getPricePerUnit());
		setDiscountPercentage(newProduct.getDiscountPercentage());
		setProductCategory(newProduct.getCategory());
	}

	public abstract Product clone();

	public void setName(String name) {
		ValidationUtils.validateName(name);
		this.name = name;
	}

	public void setPricePerUnit(double pricePerUnit) {
		ValidationUtils.validatePrice(pricePerUnit);
		this.pricePerUnit = pricePerUnit;
	}

	public void setDiscountPercentage(double discountPercentage) {
		ValidationUtils.validateDiscountPercentage(discountPercentage);
		this.discountPercentage = discountPercentage;
	}

	public void setProductCategory(ProductCategory category) {
		ValidationUtils.validateProductCategory(category);
		this.category = category;
	}

	public String getPrimaryKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getId()).append(PK_DELIMITER).append(this.getClass().getSimpleName());
		return sb.toString();
	}

}
