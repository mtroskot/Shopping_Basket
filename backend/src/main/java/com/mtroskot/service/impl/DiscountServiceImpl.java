package com.mtroskot.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mtroskot.model.entity.discount.Discount;
import com.mtroskot.repository.DiscountRepository;
import com.mtroskot.service.DiscountService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

	private final DiscountRepository discountRepository;

	@Override
	public Optional<Discount> findById(Long id) {
		return discountRepository.findById(id);
	}

	@Override
	public Discount save(Discount discount) {
		return discountRepository.save(discount);
	}

}
