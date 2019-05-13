package com.mtroskot.service;

import java.util.Optional;

import com.mtroskot.model.entity.product.Product;

public interface ProductService {

	/**
	 * Finds product by id.
	 * 
	 * @param id
	 *            The id of product.
	 * @return Optional<Product>
	 */
	Optional<Product> findById(Long id);

	/**
	 * Saves product.
	 * 
	 * @param product
	 *            The product to be saved.
	 * @return The saved product.
	 */
	Product save(Product product);

	/**
	 * Finds all products.
	 * 
	 * @return Iterable<Product>
	 */
	Iterable<Product> findAll();
}
