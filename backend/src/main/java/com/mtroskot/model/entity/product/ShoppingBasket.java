package com.mtroskot.model.entity.product;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mtroskot.model.entity.BaseEntity;
import com.mtroskot.model.entity.auth.User;
import com.mtroskot.model.entity.discount.Discount;
import com.mtroskot.utils.validation.ValidationUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class ShoppingBasket extends BaseEntity {

	private static final long serialVersionUID = 5650342325266030811L;

	private double totalPriceWithoutDiscounts;
	private double totalPriceWithDiscounts;
	private double totalBillDiscountPercentage;

	@OneToOne
	private User user;

	@Setter(AccessLevel.NONE)
	@JsonIgnore
	@ElementCollection
	@MapKeyColumn(name = "key")
	@Column(name = "quantity")
	private Map<Product, Integer> productCountMap;

	@Setter(AccessLevel.NONE)
	@Transient
	private Map<String, List<Product>> productsMap;

	@Setter(AccessLevel.NONE)
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Discount> discounts;

	public ShoppingBasket() {
		this.productCountMap = new LinkedHashMap<>();
		this.productsMap = new LinkedHashMap<>();
		this.discounts = new HashSet<>();
	}

	public void setTotalBillDiscountPercentage(double totalBillDiscountPercentage) {
		ValidationUtils.validateDiscountPercentage(totalBillDiscountPercentage);
		this.totalBillDiscountPercentage = totalBillDiscountPercentage;
	}
}
