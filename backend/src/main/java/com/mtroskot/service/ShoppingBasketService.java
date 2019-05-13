package com.mtroskot.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mtroskot.model.entity.auth.User;
import com.mtroskot.model.entity.discount.Discount;
import com.mtroskot.model.entity.product.Product;
import com.mtroskot.model.entity.product.ShoppingBasket;

public interface ShoppingBasketService {

	/**
	 * Calculates the total price of {@code basket} without discounts.
	 * 
	 * @param basket
	 *            The ShoppingBasket whose total price will be calculated.
	 * @return The ShoppingBasket whose price was requested.
	 */
	ShoppingBasket getTotalPriceWithoutDiscounts(ShoppingBasket basket);

	/**
	 * Calculates the total price of {@code basket} with discounts.
	 * 
	 * @param basket
	 *            The ShoppingBasket whose total price will be calculated.
	 * @return The ShoppingBasket whose price was requested.
	 */
	ShoppingBasket calculateTotalPriceWithDiscounts(ShoppingBasket basket);

	/**
	 * Adds 1 product in {@code basket}.
	 * 
	 * @param basket
	 *            The ShoppingBasket in which the product will be added.
	 * @param product
	 *            The Product which will be added in basket.
	 */
	void addProduct(ShoppingBasket basket, Product product);

	/**
	 * Adds one or multiple products in {@code basket} based on {@code quantity}.
	 * 
	 * @param basket
	 *            The Shopping basket in which the products will be added.
	 * @param product
	 *            The Product we want to add.
	 * @param quantity
	 *            The amount of products we want to add.
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	void addProducts(ShoppingBasket basket, Product product, int quantity);

	/**
	 * Updates 1 product in {@code basket}.
	 * 
	 * @param basket
	 *            The ShoppingBasket in which the product will be updated.
	 * @param product
	 *            The product we want to update.
	 */
	void updateProduct(ShoppingBasket basket, Product product);

	/**
	 * Removes 1 product from basket.
	 * 
	 * @param basket
	 *            The ShoppingBasket from which the product will be removed.
	 * @param product
	 *            The product we want to remove.
	 */
	void removeProduct(ShoppingBasket basket, Product product);

	/**
	 * Removes multiple products from basket based on {@code quantity}. If the
	 * product is {@code Butter} then {@code quantity} of {@code Butter} will be
	 * removed from basket.
	 * 
	 * @param basket
	 *            The ShoppingBasket from which the products will be removed.
	 * @param product
	 *            The Product we want to remove from basket.
	 * @param quantity
	 *            The number of specified products we want to remove from basket.
	 */
	void removeProducts(ShoppingBasket basket, Product product, int quantity);

	/**
	 * Removes all same {@code product} that are in the {@code basket}. If the
	 * product is {@code Butter} then all {@code Butter} will be removed from
	 * basket.
	 * 
	 * @param basket
	 *            The ShoppingBasket from which the products will be removed.
	 * @param product
	 *            The Product we want to remove from basket.
	 */
	void removeAllSameProducts(ShoppingBasket basket, Product product);

	/**
	 * Adds discount to basket.
	 * 
	 * @param basket
	 *            The ShoppingBasket to which the discount will be added.
	 * @param discount
	 *            The discount to be added.
	 */
	void addDiscount(ShoppingBasket basket, Discount discount);

	/**
	 * Finds basket by user. If not present returns new empty basket.
	 * 
	 * @param user
	 *            The User which basket we want to find.
	 * @return The ShoppingBasket of user.
	 */
	ShoppingBasket findByUser(User user);

	/**
	 * Saved basket.
	 * 
	 * @param basket
	 *            The ShoppingBasket to be saved.
	 * @return The saved basket.
	 */
	ShoppingBasket save(ShoppingBasket basket);

	/**
	 * Reset basket to inital state.
	 * 
	 * @param basket
	 *            The ShoppingBasket to be reseted.
	 */
	void empty(ShoppingBasket basket);

}
