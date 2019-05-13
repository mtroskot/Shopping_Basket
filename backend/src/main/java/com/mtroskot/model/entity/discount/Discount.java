package com.mtroskot.model.entity.discount;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import com.mtroskot.model.entity.BaseEntity;
import com.mtroskot.model.entity.product.Product;
import com.mtroskot.utils.validation.ValidationUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = { "productToBuy", "productToApplyDiscount" })
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Discount extends BaseEntity {

	private static final long serialVersionUID = -4964217251527992939L;

	private int amountOfProductsToBuy;

	@OneToOne(cascade = CascadeType.MERGE)
	private Product productToBuy;

	@OneToOne(cascade = CascadeType.MERGE)
	private Product productToApplyDiscount;

	@Enumerated(EnumType.STRING)
	private DiscountType discountType;

	public Discount(int amountOfProductsToBuy, Product productToBuy, Product productToApplyDiscount, DiscountType discountType) {
		setAmountOfProductsToBuy(amountOfProductsToBuy);
		setProductToBuy(productToBuy);
		setProductToApplyDiscount(productToApplyDiscount);
		this.discountType = discountType;
	}

	public static enum DiscountType {
		BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE, BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER,
	}

	public void setAmountOfProductsToBuy(int amountOfProductsToBuy) {
		ValidationUtils.validateProductDiscountAmount(amountOfProductsToBuy);
		this.amountOfProductsToBuy = amountOfProductsToBuy;
	}

	public void setProductToBuy(Product productToBuy) {
		ValidationUtils.validateProduct(productToBuy);
		this.productToBuy = productToBuy;
	}

	public void setProductToGetDiscount(Product productToGetDiscount) {
		ValidationUtils.validateProduct(productToGetDiscount);
		this.productToApplyDiscount = productToGetDiscount;
	}

}
