package com.mtroskot.service.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.mtroskot.exception.AppException;
import com.mtroskot.model.entity.auth.User;
import com.mtroskot.model.entity.discount.Discount;
import com.mtroskot.model.entity.discount.FreeProductDiscount;
import com.mtroskot.model.entity.discount.ProductPriceDiscount;
import com.mtroskot.model.entity.product.Bread;
import com.mtroskot.model.entity.product.Butter;
import com.mtroskot.model.entity.product.Milk;
import com.mtroskot.model.entity.product.Product;
import com.mtroskot.model.entity.product.ShoppingBasket;
import com.mtroskot.repository.ShoppingBasketRepositroy;
import com.mtroskot.service.ShoppingBasketService;

@RunWith(SpringRunner.class)
public class ShoppingBasketServiceTest {

	private ShoppingBasketService shoppingBasketService;
	@Mock
	private ShoppingBasketRepositroy basketRepository;
	private final double DELTA = 0.001;
	private String basketNullMessage = "Supplied basket is null";
	private String productNullMessage = "Supplied product is null";

	@Before
	public void setup() {
		shoppingBasketService = new ShoppingBasketServiceImpl(basketRepository);

	}

	/* Basket is null should throw AppException */
	@Test
	public void getTotalPriceWithoutDiscountsTest1() {
		try {
			shoppingBasketService.getTotalPriceWithoutDiscounts(null);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/* Empty basket, total price without discount should be 0 */
	@Test
	public void getTotalPriceWithoutDiscountsTest2() {
		ShoppingBasket basket = new ShoppingBasket();

		shoppingBasketService.getTotalPriceWithoutDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 0", 0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0", 0, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/* Empty basket, total price without discount should be 0 */
	@Test
	public void getTotalPriceWithoutDiscountsTest3() {
		ShoppingBasket basket = new ShoppingBasket();
		basket.setTotalBillDiscountPercentage(0.2);

		shoppingBasketService.getTotalPriceWithoutDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 0", 0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0.2", 0.2, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/* 3 breads in basket, total price without discount should be 21.45 */
	@Test
	public void getTotalPriceWithoutDiscountsTest4() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 7.15, 0, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, bread, 3);

		shoppingBasketService.getTotalPriceWithoutDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 21.45", 21.45, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 0", 0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0", 0, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/* 3 breads in basket, total price without discount should be 21.45 */
	@Test
	public void getTotalPriceWithoutDiscountsTest5() {
		ShoppingBasket basket = new ShoppingBasket();
		basket.setTotalBillDiscountPercentage(0.2);
		Product bread = new Bread("Black", 7.15, 0, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, bread, 3);

		shoppingBasketService.getTotalPriceWithoutDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 21.45", 21.45, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 0", 0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0.2", 0.2, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/*
	 * 3 breads, 1 milk, 2 butters in basket, total price without discount should be
	 * 46.02
	 */
	@Test
	public void getTotalPriceWithoutDiscountsTest6() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 7.15, 0, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, bread, 3);
		Product milk = new Milk("Milk", 4.35, 0, new Date());
		shoppingBasketService.addProducts(basket, milk, 1);
		;
		Product butter = new Butter("Butter", 10.11, 0, new Date());
		shoppingBasketService.addProducts(basket, butter, 2);

		shoppingBasketService.getTotalPriceWithoutDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 46.02", 46.02, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 0", 0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0", 0, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/*
	 * 3 breads, 1 milk, 2 butters in basket, total price without discount should be
	 * 45.25
	 */
	@Test
	public void getTotalPriceWithoutDiscountsTest7() {
		ShoppingBasket basket = new ShoppingBasket();
		basket.setTotalBillDiscountPercentage(0.2);
		Product bread = new Bread("Black", 7.15, 0, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, bread, 3);
		Product milk = new Milk("Milk", 4.35, 0, new Date());
		shoppingBasketService.addProducts(basket, milk, 1);
		Product butter = new Butter("Butter", 10.11, 0, new Date());
		shoppingBasketService.addProducts(basket, butter, 2);

		shoppingBasketService.getTotalPriceWithoutDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 46.02", 46.02, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 0", 0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0.2", 0.2, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/* Basket is null should throw AppException */
	@Test
	public void getTotalPriceWithDiscountsTest1() {
		ShoppingBasket basket = null;
		try {
			ReflectionTestUtils.invokeMethod(shoppingBasketService, "getTotalPriceWithDiscounts", basket);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/* Empty basket, total price with discount should be 0 */
	@Test
	public void getTotalPriceWithDiscountsTest2() {
		ShoppingBasket basket = new ShoppingBasket();

		ReflectionTestUtils.invokeMethod(shoppingBasketService, "getTotalPriceWithDiscounts", basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 0", 0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0", 0, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/* Empty basket, total price with discount should be 0 */
	@Test
	public void getTotalPriceWithDiscountsTest3() {
		ShoppingBasket basket = new ShoppingBasket();
		basket.setTotalBillDiscountPercentage(0.2);

		ReflectionTestUtils.invokeMethod(shoppingBasketService, "getTotalPriceWithDiscounts", basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price should be 0", 0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0.2", 0.2, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/* 3 breads in basket, total price with discount should be 21.45 */
	@Test
	public void getTotalPriceWithDiscountsTest4() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 7.15, 0, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, bread, 3);

		ReflectionTestUtils.invokeMethod(shoppingBasketService, "getTotalPriceWithDiscounts", basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 21.45", 21.45, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0", 0, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/* Basket has 2 products,discount is 40% sum should be 6 */
	@Test
	public void getTotalPriceWithDiscountsTest5() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 5, 0.4, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 2);

		ReflectionTestUtils.invokeMethod(shoppingBasketService, "getTotalPriceWithDiscounts", basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 6.0", 6.0, basket.getTotalPriceWithDiscounts(), DELTA);
	}

	/* Basket has 5 products,sum should be 14.0 */
	@Test
	public void getTotalPriceWithDiscountsTest6() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 5, 0.5, new Timestamp(new Date().getTime()));
		Product prod2 = new Bread("White", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 2);
		shoppingBasketService.addProducts(basket, prod2, 3);

		ReflectionTestUtils.invokeMethod(shoppingBasketService, "getTotalPriceWithDiscounts", basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 14.0", 14.0, basket.getTotalPriceWithDiscounts(), DELTA);
	}

	/*
	 * 3 breads in basket,basket discount is 0.2, total price with discount should
	 * be 17.16
	 */
	@Test
	public void getTotalPriceWithDiscountsTest7() {
		ShoppingBasket basket = new ShoppingBasket();
		basket.setTotalBillDiscountPercentage(0.2);
		Product bread = new Bread("Black", 7.15, 0, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, bread, 3);

		ReflectionTestUtils.invokeMethod(shoppingBasketService, "getTotalPriceWithDiscounts", basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 17.16", 17.16, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0.2", 0.2, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/*
	 * 3 breads, 1 milk, 2 butters in basket, total price with discount should be
	 * 37.533
	 */
	@Test
	public void getTotalPriceWithDiscountsTest8() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 7.15, 0.2, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, bread, 3);
		Product milk = new Milk("Milk", 4.35, 0.5, new Date());
		shoppingBasketService.addProducts(basket, milk, 1);
		Product butter = new Butter("Butter", 10.11, 0.1, new Date());
		shoppingBasketService.addProducts(basket, butter, 2);

		ReflectionTestUtils.invokeMethod(shoppingBasketService, "getTotalPriceWithDiscounts", basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 37.533", 37.533, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0", 0, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/*
	 * 3 breads, 1 milk, 2 butters in basket, total price with discount should be
	 * 36.816
	 */
	@Test
	public void getTotalPriceWithDiscountsTest9() {
		ShoppingBasket basket = new ShoppingBasket();
		basket.setTotalBillDiscountPercentage(0.2);
		Product bread = new Bread("Black", 7.15, 0, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, bread, 3);
		Product milk = new Milk("Milk", 4.35, 0, new Date());
		shoppingBasketService.addProducts(basket, milk, 1);
		Product butter = new Butter("Butter", 10.11, 0, new Date());
		shoppingBasketService.addProducts(basket, butter, 2);

		ReflectionTestUtils.invokeMethod(shoppingBasketService, "getTotalPriceWithDiscounts", basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 36.816", 36.816, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Discount percentage should be 0.2", 0.2, basket.getTotalBillDiscountPercentage(), DELTA);
		Assert.assertEquals("Discount size is 0", 0, basket.getDiscounts().size());
	}

	/* Basket is null should throw AppException */
	@Test
	public void checkIfBasketIsNullTest1() {
		ShoppingBasket basket = null;

		try {
			ReflectionTestUtils.invokeMethod(shoppingBasketService, "checkIfBasketIsNull", basket);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/* Basket is not null should pass validation */
	@Test
	public void checkIfBasketIsNullTest2() {
		ShoppingBasket basket = new ShoppingBasket();

		ReflectionTestUtils.invokeMethod(shoppingBasketService, "checkIfBasketIsNull", basket);
	}

	/* Basket is null,product is null should throw AppException */
	@Test
	public void checkIfBasketOrProductIsNullTest1() {
		ShoppingBasket basket = null;
		Product prod = null;

		try {
			ReflectionTestUtils.invokeMethod(shoppingBasketService, "checkIfBasketOrProductIsNull", basket, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/* Basket is null,product is not null should throw AppException */
	@Test
	public void checkIfBasketOrProductIsNullTest2() {
		ShoppingBasket basket = null;
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		try {
			ReflectionTestUtils.invokeMethod(shoppingBasketService, "checkIfBasketOrProductIsNull", basket, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/* Basket is not null, product is null should throw AppException */
	@Test
	public void checkIfBasketOrProductIsNullTest3() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = null;

		try {
			ReflectionTestUtils.invokeMethod(shoppingBasketService, "checkIfBasketOrProductIsNull", basket, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be productNullMessage", productNullMessage, e.getMessage());
		}
	}

	/* Basket is not null should pass validation */
	@Test
	public void checkIfBasketOrProductIsNullTest4() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		ReflectionTestUtils.invokeMethod(shoppingBasketService, "checkIfBasketOrProductIsNull", basket, prod);
	}

	/* Basket is null,product is null, should throw AppException */
	@Test
	public void removeAllSameProductsTest1a() {
		try {
			shoppingBasketService.removeAllSameProducts(null, null);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/* Basket is null,product is not null, should throw AppException */
	@Test
	public void removeAllSameProductsTest1b() {
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		try {
			shoppingBasketService.removeAllSameProducts(null, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/* Basket is not null,product is null, should throw AppException */
	@Test
	public void removeAllSameProductsTest1c() {
		ShoppingBasket basket = new ShoppingBasket();
		try {
			shoppingBasketService.removeAllSameProducts(basket, null);
		} catch (AppException e) {
			Assert.assertEquals("Message should be productNullMessage", productNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket product map has two product categories, after remove should have only
	 * one
	 */
	@Test
	public void removeAllSameProductsTest1() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Milk("Milk", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 2);
		shoppingBasketService.addProducts(basket, prod2, 3);

		shoppingBasketService.removeAllSameProducts(basket, prod);

		Assert.assertEquals("Basket product map isEmpty false", false, basket.getProductsMap().isEmpty());
		Assert.assertEquals("Basket product map does not contain prod", true, !basket.getProductsMap().containsKey(prod.getPrimaryKey()));
		Assert.assertEquals("Basket product map does contain prod2", true, basket.getProductsMap().containsKey(prod2.getPrimaryKey()));
		Assert.assertEquals("Basket product map does contain prod2 list size is 3", 3, basket.getProductsMap().get(prod2.getPrimaryKey()).size());
	}

	/*
	 * Basket product map has one product category, after remove basket should empty
	 */
	@Test
	public void removeAllSameProductsTest2() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 3);

		shoppingBasketService.removeAllSameProducts(basket, prod);

		Assert.assertEquals("Basket product map isEmpty true", true, basket.getProductsMap().isEmpty());
		Assert.assertEquals("Basket product map does not contain prod", true, !basket.getProductsMap().containsKey(prod.getPrimaryKey()));
	}

	/*
	 * Product to remove not found in basket, should throw AppException
	 */
	@Test
	public void removeAllSameProductsTest3() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Milk("Milk", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 3);

		try {
			shoppingBasketService.removeAllSameProducts(basket, prod2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be could not remove product", "Cannot remove products, no product found in basket for product " + prod2, e.getMessage());
		}
	}

	/*
	 * Basket is null, product is null, should throw AppException
	 */
	@Test
	public void removeProductsTest1() {
		try {
			shoppingBasketService.removeProducts(null, null, 2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is null, product is not null, should throw AppException
	 */
	@Test
	public void removeProductsTest2() {
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		try {
			shoppingBasketService.removeProducts(null, prod, 2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is not null, product is null, should throw AppException
	 */
	@Test
	public void removeProductsTest3() {
		ShoppingBasket basket = new ShoppingBasket();
		try {
			shoppingBasketService.removeProducts(basket, null, 2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be productNullMessage", productNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is not null, product is null, should throw AppException
	 */
	@Test
	public void removeProductsTest3a() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		try {
			shoppingBasketService.removeProducts(basket, prod, -2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be quanitity cannot be negative", "Quantity -2 cannot be less than 1", e.getMessage());
		}
	}

	/*
	 * No product found in basket, should throw AppException
	 */
	@Test
	public void removeProductsTest4() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Milk("Milk", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 3);

		try {
			shoppingBasketService.removeProducts(basket, prod2, 2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be could not remove product", "Cannot remove products, no product found in basket for product " + prod2, e.getMessage());
		}
	}

	/*
	 * Product found in basket, quantity to remove is greater than quantity in
	 * basket
	 */
	@Test
	public void removeProductsTest5() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Milk("Milk", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 2);
		shoppingBasketService.addProducts(basket, prod2, 3);

		shoppingBasketService.removeProducts(basket, prod, 4);

		Assert.assertEquals("Basket product map isEmpty false", false, basket.getProductsMap().isEmpty());
		Assert.assertEquals("Basket product map does not contain prod", true, !basket.getProductsMap().containsKey(prod.getPrimaryKey()));
		Assert.assertEquals("Basket product map does contain prod2", true, basket.getProductsMap().containsKey(prod2.getPrimaryKey()));
		Assert.assertEquals("Basket product map does contain prod2 list size is 3", 3, basket.getProductsMap().get(prod2.getPrimaryKey()).size());
	}

	/*
	 * Product found in basket, quantity to remove is less than quantity in basket
	 */
	@Test
	public void removeProductsTest6() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Milk("Milk", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 3);
		shoppingBasketService.addProducts(basket, prod2, 3);

		shoppingBasketService.removeProducts(basket, prod, 2);

		Assert.assertEquals("Basket product map isEmpty false", false, basket.getProductsMap().isEmpty());
		Assert.assertEquals("Basket product map does  contain prod", true, basket.getProductsMap().containsKey(prod.getPrimaryKey()));
		Assert.assertEquals("Basket product map does contain prod list size is 1", 1, basket.getProductsMap().get(prod.getPrimaryKey()).size());
		Assert.assertEquals("Basket product map does contain prod2", true, basket.getProductsMap().containsKey(prod2.getPrimaryKey()));
		Assert.assertEquals("Basket product map does contain prod2 list size is 3", 3, basket.getProductsMap().get(prod2.getPrimaryKey()).size());
	}

	/*
	 * Basket is null, product is null, should throw AppException
	 */
	@Test
	public void removeProductTest1() {
		try {
			shoppingBasketService.removeProduct(null, null);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is null, product is not null, should throw AppException
	 */
	@Test
	public void removeProductTest2() {
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		try {
			shoppingBasketService.removeProduct(null, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is null, product is not null, should throw AppException
	 */
	@Test
	public void removeProductTest3() {
		ShoppingBasket basket = new ShoppingBasket();
		try {
			shoppingBasketService.removeProduct(basket, null);
		} catch (AppException e) {
			Assert.assertEquals("Message should be productNullMessage", productNullMessage, e.getMessage());
		}
	}

	/*
	 * Product to remove not found, should throw AppException
	 */
	@Test
	public void removeProductTest4() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Milk("Milk", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 3);
		try {
			shoppingBasketService.removeProduct(basket, prod2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be could not remove product", "Cannot remove product, no product found in basket for product " + prod2, e.getMessage());
		}
	}

	/*
	 * Product to remove found, after remove list is not empty
	 */
	@Test
	public void removeProductTest5() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Milk("Milk", 3, 0, new Timestamp(new Date().getTime()));

		List<Product> linkedList = new LinkedList<>(Arrays.asList(prod, prod, prod));
		List<Product> linkedList2 = new LinkedList<>(Arrays.asList(prod2, prod2, prod2));
		basket.getProductsMap().put(prod.getPrimaryKey(), linkedList);
		basket.getProductsMap().put(prod2.getPrimaryKey(), linkedList2);

		shoppingBasketService.removeProduct(basket, prod);

		Assert.assertEquals("Basket product map isEmpty false", false, basket.getProductsMap().isEmpty());
		Assert.assertEquals("Basket product map does contain prod", true, basket.getProductsMap().containsKey(prod.getPrimaryKey()));
		Assert.assertEquals("Basket product map does contain prod list size is 2", 2, basket.getProductsMap().get(prod.getPrimaryKey()).size());
		Assert.assertEquals("Basket product map does contain prod2", true, basket.getProductsMap().containsKey(prod2.getPrimaryKey()));
		Assert.assertEquals("Basket product map does contain prod2 list size is 3", 3, basket.getProductsMap().get(prod2.getPrimaryKey()).size());
	}

	/*
	 * Product to remove found, after remove list is empty
	 */
	@Test
	public void removeProductTest6() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 1);

		shoppingBasketService.removeProduct(basket, prod);

		Assert.assertEquals("Basket product map isEmpty true", true, basket.getProductsMap().isEmpty());
		Assert.assertEquals("Basket product map does not contain prod", true, !basket.getProductsMap().containsKey(prod.getPrimaryKey()));
	}

	/*
	 * Basket is null, product is null, should throw AppException
	 */
	@Test
	public void updateProductTest1() {
		ShoppingBasket basket = null;
		Product prod = null;

		try {
			shoppingBasketService.updateProduct(basket, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is null, product is not null, should throw AppException
	 */
	@Test
	public void updateProductTest2() {
		ShoppingBasket basket = null;
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		try {
			shoppingBasketService.updateProduct(basket, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is not null, product is null, should throw AppException
	 */
	@Test
	public void updateProductTest3() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = null;

		try {
			shoppingBasketService.updateProduct(basket, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be productNullMessage", productNullMessage, e.getMessage());
		}
	}

	/*
	 * Product not found in basket, should throw AppException
	 */
	@Test
	public void updateProductTest4() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Bread("White", 3, 0, new Timestamp(new Date().getTime()));

		basket.getProductsMap().put(prod.getPrimaryKey(), Arrays.asList(prod));
		try {
			shoppingBasketService.updateProduct(basket, prod2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be product not found", "No product found in basket for product " + prod2, e.getMessage());
		}
	}

	/*
	 * Product found in basket and updated
	 */
	@Test
	public void updateProductTest5() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Bread("Black", 3, 0.5, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 1);

		shoppingBasketService.updateProduct(basket, prod2);

		List<Product> productList = basket.getProductsMap().get(prod.getPrimaryKey());
		Assert.assertEquals("ProductList size is 1", 1, basket.getProductsMap().get(prod.getPrimaryKey()).size());
		Assert.assertEquals("Last product in productList should eqault to prod2", true, productList.get(productList.size() - 1).equals(prod2));
	}

	/*
	 * Basket is null, product is null, should throw AppException
	 */
	@Test
	public void addProductsTest1() {
		ShoppingBasket basket = null;
		Product prod = null;

		try {
			shoppingBasketService.addProducts(basket, prod, -2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is null, product is not null, should throw AppException
	 */
	@Test
	public void addProductsTest2() {
		ShoppingBasket basket = null;
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		try {
			shoppingBasketService.addProducts(basket, prod, 0);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is not null, product is null, should throw AppException
	 */
	@Test
	public void addProductsTest3() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = null;

		try {
			shoppingBasketService.addProducts(basket, prod, 2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be productNullMessage", productNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is not null, product is not null,quantity is less than 0, should throw
	 * AppException
	 */
	@Test
	public void addProductsTest4() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		try {
			shoppingBasketService.addProducts(basket, prod, -2);
		} catch (AppException e) {
			Assert.assertEquals("Message should be quantity cannot be less than 0", "Quantity -2 cannot be less than 1", e.getMessage());
		}
	}

	/*
	 * No products previously in basket
	 */
	@Test
	public void addProductsTest5() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 5);

		Assert.assertEquals("Map is not empty", true, !basket.getProductsMap().isEmpty());
		Assert.assertEquals("ProductList size is 5", 5, basket.getProductsMap().get(prod.getPrimaryKey()).size());
		Assert.assertEquals("ProductList[0] is prod", prod, basket.getProductsMap().get(prod.getPrimaryKey()).get(0));
	}

	/*
	 * 2 products previously in basket
	 */
	@Test
	public void addProductsTest6() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 2);

		shoppingBasketService.addProducts(basket, prod, 5);

		Assert.assertEquals("Map is not empty", true, !basket.getProductsMap().isEmpty());
		Assert.assertEquals("ProductList size is 7", 7, basket.getProductsMap().get(prod.getPrimaryKey()).size());
		Assert.assertEquals("ProductList[0] is prod", prod, basket.getProductsMap().get(prod.getPrimaryKey()).get(0));
	}

	/*
	 * 5 products previously in basket, 2 of 5 are the same as the products which is
	 * being added
	 */
	@Test
	public void addProductsTest7() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		prod.setId(1L);
		Product prod2 = new Bread("White", 3, 0, new Timestamp(new Date().getTime()));
		prod2.setId(2L);

		shoppingBasketService.addProducts(basket, prod, 2);
		shoppingBasketService.addProducts(basket, prod2, 3);

		shoppingBasketService.addProducts(basket, prod, 5);

		Assert.assertEquals("Map is not empty", true, !basket.getProductsMap().isEmpty());
		Assert.assertEquals("ProductList size is 7", 7, basket.getProductsMap().get(prod.getPrimaryKey()).size());
		Assert.assertEquals("ProductList[0] is prod", prod, basket.getProductsMap().get(prod.getPrimaryKey()).get(0));
		Assert.assertEquals("ProductList2 size is 3", 3, basket.getProductsMap().get(prod2.getPrimaryKey()).size());
		Assert.assertEquals("ProductList2[0] is prod2", prod2, basket.getProductsMap().get(prod2.getPrimaryKey()).get(0));
	}

	/*
	 * Basket is null, product is null, should throw AppException
	 */
	@Test
	public void addProductTest1() {
		ShoppingBasket basket = null;
		Product prod = null;

		try {
			shoppingBasketService.addProduct(basket, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is null, product is not null, should throw AppException
	 */
	@Test
	public void addProductTest2() {
		ShoppingBasket basket = null;
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		try {
			shoppingBasketService.addProduct(basket, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/*
	 * Basket is not null, product is null, should throw AppException
	 */
	@Test
	public void addProductTest3() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = null;

		try {
			shoppingBasketService.addProduct(basket, prod);
		} catch (AppException e) {
			Assert.assertEquals("Message should be productNullMessage", productNullMessage, e.getMessage());
		}
	}

	/*
	 * No products previously in basket
	 */
	@Test
	public void addProductTest4() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProduct(basket, prod);

		Assert.assertEquals("Map is not empty", true, !basket.getProductsMap().isEmpty());
		Assert.assertEquals("ProductList size is 1", 1, basket.getProductsMap().get(prod.getPrimaryKey()).size());
		Assert.assertEquals("ProductList[0] is prod", prod, basket.getProductsMap().get(prod.getPrimaryKey()).get(0));
	}

	/*
	 * 2 products previously in basket
	 */
	@Test
	public void addProductTest5() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));

		shoppingBasketService.addProducts(basket, prod, 2);

		shoppingBasketService.addProduct(basket, prod);

		Assert.assertEquals("Map is not empty", true, !basket.getProductsMap().isEmpty());
		Assert.assertEquals("ProductList size is 3", 3, basket.getProductsMap().get(prod.getPrimaryKey()).size());
		Assert.assertEquals("ProductList[0] is prod", prod, basket.getProductsMap().get(prod.getPrimaryKey()).get(0));
	}

	/*
	 * 5 products previously in basket, 2 of 5 are the same as the product which is
	 * being added
	 */
	@Test
	public void addProductTest6() {
		ShoppingBasket basket = new ShoppingBasket();
		Product prod = new Bread("Black", 3, 0, new Timestamp(new Date().getTime()));
		prod.setId(1L);
		Product prod2 = new Bread("White", 3, 0, new Timestamp(new Date().getTime()));
		prod2.setId(2L);

		shoppingBasketService.addProducts(basket, prod, 2);
		shoppingBasketService.addProducts(basket, prod2, 3);

		shoppingBasketService.addProduct(basket, prod);

		Assert.assertEquals("Map is not empty", true, !basket.getProductsMap().isEmpty());
		Assert.assertEquals("ProductList size is 3", 3, basket.getProductsMap().get(prod.getPrimaryKey()).size());
		Assert.assertEquals("ProductList[0] is prod", prod, basket.getProductsMap().get(prod.getPrimaryKey()).get(0));
		Assert.assertEquals("ProductList2 size is 3", 3, basket.getProductsMap().get(prod2.getPrimaryKey()).size());
		Assert.assertEquals("ProductList2[0] is prod2", prod2, basket.getProductsMap().get(prod2.getPrimaryKey()).get(0));
	}

	/* Basket is null should throw AppException */
	@Test
	public void calculateTotalPriceWithDiscountsTest1() {
		ShoppingBasket basket = null;

		try {
			shoppingBasketService.calculateTotalPriceWithDiscounts(basket);
		} catch (AppException e) {
			Assert.assertEquals("Message should be basketNullMessage", basketNullMessage, e.getMessage());
		}
	}

	/* Basket is empty */
	@Test
	public void calculateTotalPriceWithDiscountsTest2() {
		ShoppingBasket basket = new ShoppingBasket();

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Map is empty", true, basket.getProductsMap().isEmpty());
		Assert.assertEquals("ProductList size is 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("ProductList size is 0", 0, basket.getTotalPriceWithDiscounts(), DELTA);
	}

	/* Basket has 5 products, no discounts active */
	@Test
	public void calculateTotalPriceWithDiscountsTest3() {
		ShoppingBasket basket = new ShoppingBasket();

		Product prod = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Bread("White", 4, 0, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, prod, 2);
		shoppingBasketService.addProducts(basket, prod2, 3);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 22.0", 22.0, basket.getTotalPriceWithDiscounts(), DELTA);
	}

	/* Basket has 5 products, no discounts active, totalBillDiscount is 0.2 */
	@Test
	public void calculateTotalPriceWithDiscountsTest4() {
		ShoppingBasket basket = new ShoppingBasket();
		basket.setTotalBillDiscountPercentage(0.2);

		Product prod = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product prod2 = new Bread("White", 4, 0, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, prod, 2);
		shoppingBasketService.addProducts(basket, prod2, 3);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 17.6", 17.6, basket.getTotalPriceWithDiscounts(), DELTA);
	}

	/* Basket has 5 products, no discounts active, totalBillDiscount is 0.2 */
	@Test
	public void calculateTotalPriceWithDiscountsTest5() {
		ShoppingBasket basket = new ShoppingBasket();
		basket.setTotalBillDiscountPercentage(0.2);

		Product prod = new Bread("Black", 5, 0.1, new Timestamp(new Date().getTime()));
		Product prod2 = new Bread("White", 4, 0.1, new Timestamp(new Date().getTime()));
		shoppingBasketService.addProducts(basket, prod, 2);
		shoppingBasketService.addProducts(basket, prod2, 3);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 15.84", 15.84, basket.getTotalPriceWithDiscounts(), DELTA);
	}

	/*
	 * Basket has 3 products, BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE discount is
	 * active, totalBillDiscount is 0, productToBuy was found in basket and 1 free
	 * milk achieved
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest6() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 4, 0, new Date());

		Discount discount = new FreeProductDiscount(2, bread, 1, milk);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 2);
		shoppingBasketService.addProducts(basket, milk, 1);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 10.0", 10.0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 2 breads", 2, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk list contains 1 milk", 1, basket.getProductsMap().get(milk.getPrimaryKey()).size());
		Assert.assertEquals("Milk list[0] class is Milk.class", Milk.class, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk price is 0 (free milk)", 0, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
	}

	/*
	 * Basket has 3 products, BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE discount is
	 * active, totalBillDiscount is 0.1, productToBuy was found in basket and 1 free
	 * milk achieved
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest7() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 4, 0, new Date());
		basket.setTotalBillDiscountPercentage(0.1);

		Discount discount = new FreeProductDiscount(2, bread, 1, milk);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 2);
		shoppingBasketService.addProducts(basket, milk, 1);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 9.0", 9.0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 2 breads", 2, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk list contains 1 milk", 1, basket.getProductsMap().get(milk.getPrimaryKey()).size());
		Assert.assertEquals("Milk list[0] class is Milk.class", Milk.class, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk price is 0 (free milk)", 0, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
	}

	/*
	 * Basket has 2 products, BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE discount is
	 * active, totalBillDiscount is 0, productToBuy was not found in basket
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest8() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product butter = new Butter("Butter", 2, 0, new Date());
		Product milk = new Milk("Milk", 4, 0, new Date());

		Discount discount = new FreeProductDiscount(2, butter, 1, milk);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 2);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 10.0", 10, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 2 breads", 2, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Basket does not contain milk", true, !basket.getProductsMap().containsKey(milk.getPrimaryKey()));
	}

	/*
	 * Basket has 3 products, BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE discount is
	 * active, totalBillDiscount is 0, productToBuy was found in basket, one free
	 * bread
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest9() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));

		Discount discount = new FreeProductDiscount(2, bread, 1, bread);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 3);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 10.0", 10, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 3 breads", 3, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
	}

	/*
	 * Basket has 5 products, BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE discount is
	 * active, totalBillDiscount is 0, productToBuy was found in basket, 3 free
	 * bread
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest10() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));

		Discount discount = new FreeProductDiscount(2, bread, 3, bread);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 5);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 10.0", 10, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 5 breads", 5, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
	}

	/*
	 * Basket has 2 products, BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE discount is
	 * active, totalBillDiscount is 0, productToBuy was found in basket, no free
	 * bread because amount of productToBuy is less than needed
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest11() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));

		Discount discount = new FreeProductDiscount(4, bread, 1, bread);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 2);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 10.0", 10, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 2 breads", 2, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
	}

	/*
	 * Basket has 3 products, BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER discount is
	 * active, totalBillDiscount is 0, productToBuy was found in basket and 1 milk
	 * discount achieved
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest12() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 4, 0, new Date());

		Discount discount = new ProductPriceDiscount(2, bread, 0.5, milk);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 2);
		shoppingBasketService.addProducts(basket, milk, 1);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 12.0", 12.0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 2 breads", 2, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk list contains 1 milk", 1, basket.getProductsMap().get(milk.getPrimaryKey()).size());
		Assert.assertEquals("Milk list[0] class is Milk.class", Milk.class, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk price is 2.0", 2.0, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
	}

	/*
	 * Basket has 3 products, BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER discount is
	 * active, totalBillDiscount is 0.1, productToBuy was found in basket and 1 milk
	 * discount achieved
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest13() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 4, 0, new Date());
		basket.setTotalBillDiscountPercentage(0.1);

		Discount discount = new ProductPriceDiscount(2, bread, 0.5, milk);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 2);
		shoppingBasketService.addProducts(basket, milk, 1);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 10.8", 10.8, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 2 breads", 2, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk list contains 1 milk", 1, basket.getProductsMap().get(milk.getPrimaryKey()).size());
		Assert.assertEquals("Milk list[0] class is Milk.class", Milk.class, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk price is 2.0", 2.0, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
	}

	/*
	 * Basket has 2 products, BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER discount is
	 * active, totalBillDiscount is 0, productToBuy was not found in basket
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest14() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 4, 0, new Date());

		Discount discount = new ProductPriceDiscount(2, milk, 0.5, milk);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 2);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 10.0", 10, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 2 breads", 2, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Basket does not contain milk", true, !basket.getProductsMap().containsKey(milk.getPrimaryKey()));
	}

	/*
	 * Basket has 2 products, BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER discount is
	 * active, totalBillDiscount is 0, productToBuy was found in basket,
	 * productToGetDiscount was not found in basket
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest15() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 4, 0, new Date());

		Discount discount = new ProductPriceDiscount(2, bread, 0.5, milk);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 2);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 10.0", 10, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 2 breads", 2, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Basket does not contain milk", true, !basket.getProductsMap().containsKey(milk.getPrimaryKey()));
	}

	/*
	 * Basket has 2 products, BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER discount is
	 * active, totalBillDiscount is 0, productToBuy was found in basket,
	 * productToGetDiscount was found in basket, no discount because not enough
	 * product bought
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest16() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 4, 0, new Date());

		Discount discount = new ProductPriceDiscount(4, bread, 0.5, milk);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 2);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 10.0", 10, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 2 breads", 2, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
	}

	/*
	 * Basket has 4 products, BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER discount is
	 * active, totalBillDiscount is 0,productToBuy was found in basket,
	 * productToGetDiscount was found in basket, 2 bread discounts
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest17() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));

		Discount discount = new ProductPriceDiscount(1, bread, 0.5, bread);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 4);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 15.0", 15.0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 4 breads", 4, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
	}

	/*
	 * Basket has 4 products, BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER discount is
	 * active, totalBillDiscount is 0, productToBuy was found in basket,
	 * productToGetDiscount was found in basket, 1 bread discount
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest18() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));

		Discount discount = new ProductPriceDiscount(2, bread, 0.5, bread);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 4);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 17.5", 17.5, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 4 breads", 4, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
	}

	/*
	 * Basket has 6 products, BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER discount is
	 * active, totalBillDiscount is 0, productToBuy was found in basket,
	 * productToGetDiscount was found in basket, 1 bread discount
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest19() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 5, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 4, 0, new Date());

		Discount discount = new ProductPriceDiscount(1, bread, 0.5, milk);
		basket.getDiscounts().add(discount);
		shoppingBasketService.addProducts(basket, bread, 3);
		shoppingBasketService.addProducts(basket, milk, 3);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 21.0", 21.0, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 3 breads", 3, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk list contains 3 milk", 3, basket.getProductsMap().get(milk.getPrimaryKey()).size());
		Assert.assertEquals("Milk list[0] class is Milk.class", Milk.class, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk price is 2.0", 2.0, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
	}

	/*
	 * Basket has 3 products,
	 * BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER,BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE
	 * discount are active, totalBillDiscount is 0, no discounts applied
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest20() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 1.0, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 1.15, 0, new Date());
		Product butter = new Butter("Butter", 0.8, 0, new Date());

		Discount discount = new ProductPriceDiscount(2, butter, 0.5, bread);
		Discount discount2 = new FreeProductDiscount(3, milk, 1, milk);
		basket.getDiscounts().addAll(Arrays.asList(discount, discount2));
		shoppingBasketService.addProducts(basket, bread, 1);
		shoppingBasketService.addProducts(basket, butter, 1);
		shoppingBasketService.addProducts(basket, milk, 1);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 2.95", 2.95, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 1 bread", 1, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Bread list[0] price is 1.0", 1.0, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
		Assert.assertEquals("Butter list contains 1 butter", 1, basket.getProductsMap().get(butter.getPrimaryKey()).size());
		Assert.assertEquals("Butter list[0] class is Butter.class", Butter.class, basket.getProductsMap().get(butter.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Butter list[0] price is 0.8", 0.8, basket.getProductsMap().get(butter.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
		Assert.assertEquals("Milk list contains 1 milk", 1, basket.getProductsMap().get(milk.getPrimaryKey()).size());
		Assert.assertEquals("Milk list[0] class is Milk.class", Milk.class, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk list[0] price is 1.15", 1.15, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
	}

	/*
	 * Basket has 4 products,
	 * BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER,BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE
	 * discount are active, totalBillDiscount is 0, 50% bread discount
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest21() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 1.0, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 1.15, 0, new Date());
		Product butter = new Butter("Butter", 0.8, 0, new Date());

		Discount discount = new ProductPriceDiscount(2, butter, 0.5, bread);
		Discount discount2 = new FreeProductDiscount(3, milk, 1, milk);
		basket.getDiscounts().addAll(Arrays.asList(discount, discount2));
		shoppingBasketService.addProducts(basket, bread, 2);
		shoppingBasketService.addProducts(basket, butter, 2);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 3.10", 3.10, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 2 bread", 2, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Bread list[0] price is 0.5", 0.5, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
		Assert.assertEquals("Bread list[1] price is 1.0", 1.0, basket.getProductsMap().get(bread.getPrimaryKey()).get(1).getPricePerUnit(), DELTA);
		Assert.assertEquals("Butter list contains 2 butter", 2, basket.getProductsMap().get(butter.getPrimaryKey()).size());
		Assert.assertEquals("Butter list[0] class is Butter.class", Butter.class, basket.getProductsMap().get(butter.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Butter list[0] price is 0.8", 0.8, basket.getProductsMap().get(butter.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
		Assert.assertEquals("Butter list[1] price is 0.8", 0.8, basket.getProductsMap().get(butter.getPrimaryKey()).get(1).getPricePerUnit(), DELTA);
	}

	/*
	 * Basket has 3 products,
	 * BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER,BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE
	 * discount are active, totalBillDiscount is 0, free milk discount
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest22() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 1.0, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 1.15, 0, new Date());
		Product butter = new Butter("Butter", 0.8, 0, new Date());

		Discount discount = new ProductPriceDiscount(2, butter, 0.5, bread);
		Discount discount2 = new FreeProductDiscount(3, milk, 1, milk);
		basket.getDiscounts().addAll(Arrays.asList(discount, discount2));
		shoppingBasketService.addProducts(basket, milk, 4);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 3.45", 3.45, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Milk list contains 4 milk", 4, basket.getProductsMap().get(milk.getPrimaryKey()).size());
		Assert.assertEquals("Milk list[0] class is Milk.class", Milk.class, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk list[0] price is 0 free milk", 0, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
	}

	/*
	 * Basket has 3 products,
	 * BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER,BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE
	 * discount are active, totalBillDiscount is 0, free milk discount
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest23() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 1.0, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 1.15, 0, new Date());
		Product butter = new Butter("Butter", 0.8, 0, new Date());

		Discount discount = new ProductPriceDiscount(2, butter, 0.5, bread);
		Discount discount2 = new FreeProductDiscount(2, milk, 2, milk);
		basket.getDiscounts().addAll(Arrays.asList(discount, discount2));
		shoppingBasketService.addProducts(basket, milk, 5);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 3.45", 3.45, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Milk list contains 5 milk", 5, basket.getProductsMap().get(milk.getPrimaryKey()).size());
		Assert.assertEquals("Milk list[0] class is Milk.class", Milk.class, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk list[0] price is 0 free milk", 0, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
		Assert.assertEquals("Milk list[1] price is 0 free milk", 0, basket.getProductsMap().get(milk.getPrimaryKey()).get(1).getPricePerUnit(), DELTA);
	}

	/*
	 * Basket has 3 products,
	 * BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER,BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE
	 * discount are active, totalBillDiscount is 0, free milk discount, bread 50%
	 * discount
	 */
	@Test
	public void calculateTotalPriceWithDiscountsTest24() {
		ShoppingBasket basket = new ShoppingBasket();
		Product bread = new Bread("Black", 1.0, 0, new Timestamp(new Date().getTime()));
		Product milk = new Milk("Milk", 1.15, 0, new Date());
		Product butter = new Butter("Butter", 0.8, 0, new Date());

		Discount discount = new ProductPriceDiscount(2, butter, 0.5, bread);
		Discount discount2 = new FreeProductDiscount(3, milk, 1, milk);
		basket.getDiscounts().addAll(Arrays.asList(discount, discount2));
		shoppingBasketService.addProducts(basket, bread, 1);
		shoppingBasketService.addProducts(basket, butter, 2);
		shoppingBasketService.addProducts(basket, milk, 8);

		shoppingBasketService.calculateTotalPriceWithDiscounts(basket);

		Assert.assertEquals("Total price without discount should be 0", 0, basket.getTotalPriceWithoutDiscounts(), DELTA);
		Assert.assertEquals("Total price with discount should be 9.00", 9.00, basket.getTotalPriceWithDiscounts(), DELTA);
		Assert.assertEquals("Bread list contains 1 bread", 1, basket.getProductsMap().get(bread.getPrimaryKey()).size());
		Assert.assertEquals("Bread list[0] class is Bread.class", Bread.class, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Bread list[0] price is 0.5", 0.5, basket.getProductsMap().get(bread.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
		Assert.assertEquals("Butter list contains 2 butter", 2, basket.getProductsMap().get(butter.getPrimaryKey()).size());
		Assert.assertEquals("Butter list[0] class is Butter.class", Butter.class, basket.getProductsMap().get(butter.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Butter list[0] price is 0.8", 0.8, basket.getProductsMap().get(butter.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
		Assert.assertEquals("Milk list contains 8 milk", 8, basket.getProductsMap().get(milk.getPrimaryKey()).size());
		Assert.assertEquals("Milk list[0] class is Milk.class", Milk.class, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getClass());
		Assert.assertEquals("Milk list[0] price is 0 free milk", 0, basket.getProductsMap().get(milk.getPrimaryKey()).get(0).getPricePerUnit(), DELTA);
	}

	@Test
	public void findByUserTest1() {

		Mockito.when(basketRepository.findByUser(ArgumentMatchers.any(User.class))).thenReturn(Optional.ofNullable(null));

		User user = new User();
		ShoppingBasket findByUser = shoppingBasketService.findByUser(user);

		Assert.assertEquals("Basket user is equal to user", user, findByUser.getUser());
	}

}
