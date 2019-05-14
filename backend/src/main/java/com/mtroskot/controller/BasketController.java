package com.mtroskot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mtroskot.exception.AppException;
import com.mtroskot.model.entity.auth.User;
import com.mtroskot.model.entity.discount.Discount;
import com.mtroskot.model.entity.discount.FreeProductDiscount;
import com.mtroskot.model.entity.discount.ProductPriceDiscount;
import com.mtroskot.model.entity.product.Product;
import com.mtroskot.model.entity.product.ShoppingBasket;
import com.mtroskot.service.ProductService;
import com.mtroskot.service.ShoppingBasketService;
import com.mtroskot.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/basket")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BasketController {

	private final ProductService productService;
	private final ShoppingBasketService shoppingBasketService;
	private final UserService userService;

	/**
	 * Gets basket by user.
	 * 
	 * @param userId
	 *            The id of user.
	 * @return The basket of user.
	 */
	@GetMapping("/get/{userId}")
	public ShoppingBasket getBasketByUser(@PathVariable("userId") String userId) {
		User user = userService.findById(Long.parseLong(userId)).orElseThrow(() -> new AppException("No user found for user id " + userId));
		return shoppingBasketService.findByUser(user);
	}

	/**
	 * Adds product to basket.
	 * 
	 * @param userId
	 *            The id of user.
	 * @param productId
	 *            The id of product to be added.
	 * @param quantity
	 *            The quantity of products to be added.
	 * @return ResponseEntity<ShoppingBasket>
	 */
	@PostMapping("/add/{quantity}/{productId}")
	public ResponseEntity<ShoppingBasket> addProduct(@RequestBody String userId, @PathVariable("productId") String productId, @PathVariable("quantity") String quantity) {
		try {
			User user = userService.findById(Long.parseLong(userId)).orElseThrow(() -> new AppException("No user found for user id " + userId));
			ShoppingBasket basket = shoppingBasketService.findByUser(user);
			Product product = productService.findById(Long.parseLong(productId)).orElseThrow(() -> new AppException("No product found for product id " + productId));
			shoppingBasketService.addProducts(basket, product, Integer.parseInt(quantity));
			ShoppingBasket savedBasket = shoppingBasketService.save(basket);
			return new ResponseEntity<ShoppingBasket>(savedBasket, HttpStatus.CREATED);
		} catch (NumberFormatException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * Adds {@code FreeProductDiscount} to basket.
	 * 
	 * @param userId
	 *            The id of user.
	 * @param quantityToBuy
	 *            The quantity of product to buy to get discount.
	 * @param productToBuy
	 *            The product to buy to get discount.
	 * @param quantityToGet
	 *            The quantity of product the get for free.
	 * @param productToGetFree
	 *            The product to get for free.
	 * @return ResponseEntity<ShoppingBasket>
	 */
	@PostMapping("/freeProductDiscount/{userId}/{quantityToBuy}/{productToBuy}/{quantityToGet}/{productToGetFree}")
	public ResponseEntity<ShoppingBasket> addFreeProductDiscount(@PathVariable("userId") String userId, @PathVariable("quantityToBuy") String quantityToBuy,
			@PathVariable("productToBuy") String productToBuy, @PathVariable("quantityToGet") String quantityToGet, @PathVariable("productToGetFree") String productToGetFree) {
		try {
			Product pToBuy = productService.findById(Long.parseLong(productToBuy)).orElseThrow(() -> new AppException("Product to buy not found by id " + productToBuy));
			Product pToGetFree = productService.findById(Long.parseLong(productToGetFree))
					.orElseThrow(() -> new AppException("Product to get free not found by id" + productToBuy));
			User user = userService.findById(Long.parseLong(userId)).orElseThrow(() -> new AppException("No user found for user id " + userId));
			ShoppingBasket basket = shoppingBasketService.findByUser(user);

			Discount freeProductDiscount = new FreeProductDiscount(Integer.parseInt(quantityToBuy), pToBuy, Integer.parseInt(quantityToGet), pToGetFree);

			shoppingBasketService.addDiscount(basket, freeProductDiscount);
			ShoppingBasket savedBasket = shoppingBasketService.save(basket);
			return new ResponseEntity<ShoppingBasket>(savedBasket, HttpStatus.CREATED);
		} catch (NumberFormatException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * Adds {@code ProductPriceDiscount} to basket.
	 * 
	 * @param userId
	 *            The id of user.
	 * @param quantityToBuy
	 *            The quantity of product to buy to get discount.
	 * @param productToBuy
	 *            The product to buy to get discount.
	 * @param discountPercentage
	 *            The discount percentage.
	 * @param productToGetDiscount
	 *            The product to apply discount price.
	 * @return ResponseEntity<ShoppingBasket>
	 */
	@PostMapping("/productPriceDiscount/{userId}/{quantityToBuy}/{productToBuy}/{discountPercentage}/{productToGetDiscount}")
	public ResponseEntity<ShoppingBasket> addProductPriceDiscount(@PathVariable("userId") String userId, @PathVariable("quantityToBuy") String quantityToBuy,
			@PathVariable("productToBuy") String productToBuy, @PathVariable("discountPercentage") String discountPercentage,
			@PathVariable("productToGetDiscount") String productToGetDiscount) {
		try {
			Product pToBuy = productService.findById(Long.parseLong(productToBuy)).orElseThrow(() -> new AppException("Product to buy not found by id " + productToBuy));
			Product pToGetFree = productService.findById(Long.parseLong(productToGetDiscount))
					.orElseThrow(() -> new AppException("Product to get price discount not found by id" + productToGetDiscount));
			User user = userService.findById(Long.parseLong(userId)).orElseThrow(() -> new AppException("No user found for user id " + userId));
			ShoppingBasket basket = shoppingBasketService.findByUser(user);
			Discount priceDiscount = new ProductPriceDiscount(Integer.parseInt(quantityToBuy), pToBuy, Double.parseDouble(discountPercentage), pToGetFree);

			shoppingBasketService.addDiscount(basket, priceDiscount);
			ShoppingBasket savedBasket = shoppingBasketService.save(basket);
			return new ResponseEntity<ShoppingBasket>(savedBasket, HttpStatus.CREATED);
		} catch (NumberFormatException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * Resets basket to inital state.
	 * 
	 * @param userId
	 *            The id of user whose basket we want to reset.
	 * @return ResponseEntity<ShoppingBasket>
	 */
	@PostMapping("/empty")
	public ResponseEntity<ShoppingBasket> emptyBasket(@RequestBody String userId) {
		User user = userService.findById(Long.parseLong(userId)).orElseThrow(() -> new AppException("No user found for user id " + userId));
		ShoppingBasket basket = shoppingBasketService.findByUser(user);
		shoppingBasketService.empty(basket);
		ShoppingBasket savedBasket = shoppingBasketService.save(basket);
		return new ResponseEntity<ShoppingBasket>(savedBasket, HttpStatus.OK);
	}

}
