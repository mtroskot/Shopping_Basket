package com.mtroskot.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mtroskot.model.entity.product.Product;
import com.mtroskot.repository.ProductRepository;
import com.mtroskot.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Override
	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Iterable<Product> findAll() {
		return productRepository.findAll();
	}

}
