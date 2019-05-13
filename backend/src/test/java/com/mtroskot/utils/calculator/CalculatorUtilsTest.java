package com.mtroskot.utils.calculator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.mtroskot.exception.AppException;

@RunWith(SpringRunner.class)
public class CalculatorUtilsTest {

	private final double DELTA = 0.001;

	@Test
	public void calculateDiscountPrice1() {
		double discountPrice1 = CalculatorUtils.calculateDiscountPrice(10, 0.1);
		double discountPrice2 = CalculatorUtils.calculateDiscountPrice(25, 0.5);
		double discountPrice3 = CalculatorUtils.calculateDiscountPrice(1, 0.9);
		double discountPrice4 = CalculatorUtils.calculateDiscountPrice(0, 0.1);
		double discountPrice5 = CalculatorUtils.calculateDiscountPrice(0, 0);
		double discountPrice6 = CalculatorUtils.calculateDiscountPrice(9.5, 0);
		double discountPrice7 = CalculatorUtils.calculateDiscountPrice(9.5, -0);
		double discountPrice8 = CalculatorUtils.calculateDiscountPrice(-0, 0);

		Assert.assertEquals("Discount price 1 should be 9", 9, discountPrice1, DELTA);
		Assert.assertEquals("Discount price 2 should be 12.5", 12.5, discountPrice2, DELTA);
		Assert.assertEquals("Discount price 3 should be 0.1", 0.1, discountPrice3, DELTA);
		Assert.assertEquals("Discount price 4 should be 0", 0, discountPrice4, DELTA);
		Assert.assertEquals("Discount price 5 should be 0", 0, discountPrice5, DELTA);
		Assert.assertEquals("Discount price 6 should be 9.5", 9.5, discountPrice6, DELTA);
		Assert.assertEquals("Discount price 7 should be 9.5", 9.5, discountPrice7, DELTA);
		Assert.assertEquals("Discount price 8 should be 0", 0, discountPrice8, DELTA);
	}

	@Test
	public void calculateDiscountPrice2() {
		try {
			CalculatorUtils.calculateDiscountPrice(10, -0.5);
		} catch (AppException e) {
			Assert.assertEquals("Message should be discountInvalid", "Discount value -0.5 is invalid", e.getLocalizedMessage());
		}
		try {
			CalculatorUtils.calculateDiscountPrice(25, 1.5);
		} catch (AppException e) {
			Assert.assertEquals("Message should be discountInvalid", "Discount value 1.5 is invalid", e.getLocalizedMessage());
		}
		try {
			CalculatorUtils.calculateDiscountPrice(0, -0.5);
		} catch (AppException e) {
			Assert.assertEquals("Message should be discountInvalid", "Discount value -0.5 is invalid", e.getLocalizedMessage());
		}
		try {
			CalculatorUtils.calculateDiscountPrice(-2, 0);
		} catch (AppException e) {
			Assert.assertEquals("Message should be priceInvalid", "Price without discount -2.0 cannot be less than 0", e.getLocalizedMessage());
		}
		try {
			CalculatorUtils.calculateDiscountPrice(-2, -10);
		} catch (AppException e) {
			Assert.assertEquals("Message should be discountInvalid", "Discount value -10.0 is invalid", e.getLocalizedMessage());
		}
	}

}
