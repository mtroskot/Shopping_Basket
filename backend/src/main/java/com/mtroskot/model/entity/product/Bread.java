package com.mtroskot.model.entity.product;

import java.sql.Timestamp;

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
public class Bread extends Product {


	private static final long serialVersionUID = -2278127635116622976L;

	private Timestamp bakedTimestamp;

	public Bread(String name, double pricePerUnit, double discountPercentage, Timestamp bakedTimestamp) {
		super(name, pricePerUnit, discountPercentage, new ProductCategory(Category.BREAD));
		this.bakedTimestamp = bakedTimestamp;
	}

	public Bread(Bread newBread) {
		super(newBread);
		this.bakedTimestamp = newBread.getBakedTimestamp();
	}
	
	@Override
	public Product clone() {
		return new Bread(this);
	}

}
