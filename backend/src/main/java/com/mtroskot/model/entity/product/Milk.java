package com.mtroskot.model.entity.product;

import java.util.Date;

import javax.persistence.Entity;

import com.mtroskot.model.entity.product.ProductCategory.Category;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Milk extends Product {
	private static final long serialVersionUID = -1585931840565166777L;
	private Date expiryDate;

	public Milk(String name, double pricePerUnit, double discountPercentage, Date expiryDate) {
		super(name, pricePerUnit, discountPercentage, new ProductCategory(Category.MILK));
		this.expiryDate = expiryDate;
	}

	public Milk(Milk newMilk) {
		super(newMilk);
		this.expiryDate = newMilk.getExpiryDate();
	}

	@Override
	public Product clone() {
		return new Milk(this);
	}

}
