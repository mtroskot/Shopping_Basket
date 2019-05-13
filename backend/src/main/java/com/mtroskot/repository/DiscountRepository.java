package com.mtroskot.repository;

import org.springframework.data.repository.CrudRepository;

import com.mtroskot.model.entity.discount.Discount;

public interface DiscountRepository extends CrudRepository<Discount, Long> {

}
