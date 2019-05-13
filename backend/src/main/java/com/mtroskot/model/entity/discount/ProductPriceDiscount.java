package com.mtroskot.model.entity.discount;

import javax.persistence.Entity;

import com.mtroskot.model.entity.product.Product;
import com.mtroskot.utils.validation.ValidationUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "discountPercentage" })
@Entity
public class ProductPriceDiscount extends Discount {

	private static final long serialVersionUID = -478653487689763165L;

	private double discountPercentage;

	public ProductPriceDiscount(int amountOfProductsToBuy, Product productToBuy, double discountPercentage, Product productToGetDiscount) {
		super(amountOfProductsToBuy, productToBuy, productToGetDiscount, DiscountType.BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER);
		setDiscountPercentage(discountPercentage);
	}

	public void setDiscountPercentage(double discountPercentage) {
		ValidationUtils.validateDiscountPercentage(discountPercentage);
		this.discountPercentage = discountPercentage;
	}

}
