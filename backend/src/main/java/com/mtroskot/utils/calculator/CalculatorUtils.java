package com.mtroskot.utils.calculator;

import com.mtroskot.exception.AppException;

public class CalculatorUtils {
	
	/**
	 * Calculates discount price
	 * 
	 * @param priceWithoutDiscount
	 *            The price before discount
	 * @param discount
	 *            The discount to be applied
	 * @return Double The price after discount
	 */
	public static double calculateDiscountPrice(double priceWithoutDiscount, double discountPercentage) {
		if (discountPercentage > 1 || discountPercentage < 0) {
			throw new AppException("Discount value " + discountPercentage + " is invalid");
		}
		if (priceWithoutDiscount < 0) {
			throw new AppException("Price without discount " + priceWithoutDiscount + " cannot be less than 0");
		}
		return priceWithoutDiscount - (priceWithoutDiscount * discountPercentage);
	}
}
