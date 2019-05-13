package com.mtroskot.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mtroskot.model.entity.auth.User;
import com.mtroskot.model.entity.product.ShoppingBasket;

@Repository
public interface ShoppingBasketRepositroy extends CrudRepository<ShoppingBasket, Long> {

	Optional<ShoppingBasket> findByUser(User user);

}
