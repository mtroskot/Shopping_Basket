package com.mtroskot.configuration;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mtroskot.model.entity.auth.Role;
import com.mtroskot.model.entity.auth.Role.RoleType;
import com.mtroskot.model.entity.auth.User;
import com.mtroskot.model.entity.product.Bread;
import com.mtroskot.model.entity.product.Butter;
import com.mtroskot.model.entity.product.Milk;
import com.mtroskot.model.entity.product.Product;
import com.mtroskot.service.DiscountService;
import com.mtroskot.service.ProductService;
import com.mtroskot.service.RoleService;
import com.mtroskot.service.ShoppingBasketService;
import com.mtroskot.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@Profile("!prod")
public class DevelopmentConfig {

	/**
	 * Populates database with test data.
	 * 
	 * @param roleService
	 * @param userService
	 * @param discountService
	 * @param basketService
	 * @param productService
	 * @return
	 */
	@Bean
	public CommandLineRunner dataLoader(RoleService roleService, UserService userService, DiscountService discountService, ShoppingBasketService basketService,
			ProductService productService) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				log.info("Starting dataLoader");
				/* creating user */
				Role role = new Role();
				role.setType(RoleType.USER);
				User user = new User("troki", "mtroskot@hotmail.com", "MTro1234", "Marko", "Troskot", Collections.singleton(role));
				boolean encodePassword = true;
				User save = userService.save(user, encodePassword);
				System.out.println(save);

				/* adding products */
				Product bread = new Bread("Bread", 1, 0, new Timestamp(new Date().getTime()));
				productService.save(bread);
				Product butter = new Butter("Butter", 0.8, 0, new Date());
				productService.save(butter);
				Product milk = new Milk("Milk", 1.15, 0, new Date());
				productService.save(milk);
			}
		};
	}

}