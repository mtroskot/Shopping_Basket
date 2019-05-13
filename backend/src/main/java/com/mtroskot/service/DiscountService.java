package com.mtroskot.service;

import java.util.Optional;

import com.mtroskot.model.entity.discount.Discount;

public interface DiscountService {

	/**
	 * Find discount by id.
	 * 
	 * @param id
	 *            The id of discount.
	 * @return Optional<Discount>
	 */
	Optional<Discount> findById(Long id);

	/**
	 * Saves discount.
	 * 
	 * @param discount
	 *            The discount to be saved.
	 * @return The saved discount.
	 */
	Discount save(Discount discount);
}
