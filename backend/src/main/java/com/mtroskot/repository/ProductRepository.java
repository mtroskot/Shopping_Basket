package com.mtroskot.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.mtroskot.model.entity.product.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

}
