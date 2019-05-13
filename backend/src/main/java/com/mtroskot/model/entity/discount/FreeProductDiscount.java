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
@EqualsAndHashCode(callSuper = true, exclude = { "amountOfProductsToGetFree" })
@Entity
public class FreeProductDiscount extends Discount {

	private static final long serialVersionUID = 4191649529817576730L;

	private int amountOfProductsToGetFree;

	public FreeProductDiscount(int amountOfProductsToBuy, Product productToBuy, int amountOfProductsToGetFree, Product productToGetForFree) {
		super(amountOfProductsToBuy, productToBuy, productToGetForFree, DiscountType.BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE);
		setAmountOfProductsToGetFree(amountOfProductsToGetFree);
	}

	public void setAmountOfProductsToGetFree(int amountOfProductsToGetFree) {
		ValidationUtils.validateProductDiscountAmount(amountOfProductsToGetFree);
		this.amountOfProductsToGetFree = amountOfProductsToGetFree;
	}

}
