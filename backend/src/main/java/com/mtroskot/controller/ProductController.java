package com.mtroskot.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtroskot.model.entity.product.Product;
import com.mtroskot.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	/**
	 * Finds all products.
	 * 
	 * @return Iterable<Product>
	 */
	@GetMapping("/all")
	public Iterable<Product> getAllProducts() {
		return productService.findAll();
	}

}
