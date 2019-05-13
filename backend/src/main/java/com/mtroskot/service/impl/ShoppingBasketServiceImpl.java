package com.mtroskot.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mtroskot.exception.AppException;
import com.mtroskot.model.entity.auth.User;
import com.mtroskot.model.entity.discount.Discount;
import com.mtroskot.model.entity.discount.Discount.DiscountType;
import com.mtroskot.model.entity.discount.FreeProductDiscount;
import com.mtroskot.model.entity.discount.ProductPriceDiscount;
import com.mtroskot.model.entity.product.Product;
import com.mtroskot.model.entity.product.ShoppingBasket;
import com.mtroskot.repository.ShoppingBasketRepositroy;
import com.mtroskot.service.ShoppingBasketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShoppingBasketServiceImpl implements ShoppingBasketService {

	private final ShoppingBasketRepositroy basketRepository;

	@Override
	public ShoppingBasket getTotalPriceWithoutDiscounts(ShoppingBasket basket) {
		checkIfBasketIsNull(basket);
		double sum = 0;
		for (Map.Entry<String, List<Product>> entry : basket.getProductsMap().entrySet()) {
			List<Product> products = entry.getValue();
			sum += products.stream().mapToDouble(Product::getPricePerUnit).sum();
		}
		basket.setTotalPriceWithoutDiscounts(sum);
		return basket;
	}

	@Override
	public ShoppingBasket calculateTotalPriceWithDiscounts(ShoppingBasket basket) {
		checkIfBasketIsNull(basket);
		Set<Discount> discounts = basket.getDiscounts();
		for (Discount discount : discounts) {
			if (discount.getDiscountType() == DiscountType.BUY_N_PRODUCTS_GET_Y_PRODUCTS_FOR_FREE) {
				FreeProductDiscount freeProductDiscount = (FreeProductDiscount) discount;
				Product productToBuy = freeProductDiscount.getProductToBuy();
				List<Product> productListFromBasket = basket.getProductsMap().get(productToBuy.getPrimaryKey());

				if (productListFromBasket != null) { // productToBuy was found in basket, checking how many free product user has  achieved
					int amountOfFreeProductDiscountAchieved = 0;
					// if productToBuy is same as productToGetFree, than we need one more productToBuy to apply free discount
					if (productToBuy.equals(freeProductDiscount.getProductToApplyDiscount())) {
						amountOfFreeProductDiscountAchieved = productListFromBasket.size() / (freeProductDiscount.getAmountOfProductsToBuy() + 1);
					} else {
						amountOfFreeProductDiscountAchieved = productListFromBasket.size() / freeProductDiscount.getAmountOfProductsToBuy();
					}

					if (amountOfFreeProductDiscountAchieved > 0) { // user achieved the right to get free products
						Product productToGetForFree = freeProductDiscount.getProductToApplyDiscount();
						int totalFreeProductSum = amountOfFreeProductDiscountAchieved * freeProductDiscount.getAmountOfProductsToGetFree();
						List<Product> productListToApplyDiscount = basket.getProductsMap().get(productToGetForFree.getPrimaryKey());

						if (productListToApplyDiscount != null) { // productToGetForFree found in basket, applying the discount in productListToApplyDiscount
							for (int i = 0; i < totalFreeProductSum && i < productListToApplyDiscount.size(); i++) {
								Product productToApplyDiscount = productListToApplyDiscount.get(i);
								if (productToApplyDiscount.getPricePerUnit() == 0) {// product is already for free,skipping
									totalFreeProductSum++; // incrementing because this product was already free,the discount could not be used, trying to apply the discount on the next product if present
									continue;
								}
								productToApplyDiscount.setPricePerUnit(0); // setting price to 0, because the product is for free
							}
						}
					}
				}
			}
			if (discount.getDiscountType() == DiscountType.BUY_N_PRODUCTS_GET_DISCOUNT_ON_ANOTHER) {
				ProductPriceDiscount discountBasedOnAnotherProduct = (ProductPriceDiscount) discount;
				Product productToBuy = discountBasedOnAnotherProduct.getProductToBuy();
				List<Product> productListFromBasket = basket.getProductsMap().get(productToBuy.getPrimaryKey());
				if (productListFromBasket != null) { // productToBuy was found in basket, checking on how many products user has discount right
					int amountOfProductsToApplyDiscount = 0;
					// if productToBuy is same as productToGetDiscount than we can only apply half of discounts (on every second product)
					if (productToBuy.equals(discountBasedOnAnotherProduct.getProductToApplyDiscount())) {
						amountOfProductsToApplyDiscount = (productListFromBasket.size() / discountBasedOnAnotherProduct.getAmountOfProductsToBuy()) / 2;
					} else {
						amountOfProductsToApplyDiscount = productListFromBasket.size() / discountBasedOnAnotherProduct.getAmountOfProductsToBuy();
					}

					if (amountOfProductsToApplyDiscount > 0) { // user achieved the right to get discount on products
						Product productToGetDiscount = discountBasedOnAnotherProduct.getProductToApplyDiscount();
						List<Product> productListToApplyDiscount = basket.getProductsMap().get(productToGetDiscount.getPrimaryKey());

						if (productListToApplyDiscount != null) { // productToGetDiscount was in basket, applying the discount in productListToApplyDiscount
							for (int i = 0; i < amountOfProductsToApplyDiscount && i < productListToApplyDiscount.size(); i++) {
								Product productToApplyDiscount = productListToApplyDiscount.get(i);
								if (productToApplyDiscount.getDiscountPercentage() != 0) {// product has already a price discount,skipping
									amountOfProductsToApplyDiscount++; // incrementing because this product has already a price discount,the discount could not be used, trying to apply the discount on the next product if present
									continue;
								}
								double priceAfterDiscount = productToApplyDiscount.getPricePerUnit()
										- (productToApplyDiscount.getPricePerUnit() * discountBasedOnAnotherProduct.getDiscountPercentage());
								productToApplyDiscount.setPricePerUnit(priceAfterDiscount);
							}
						}
					}
				}
			}
		}

		getTotalPriceWithDiscounts(basket);
		return basket;
	}

	/**
	 * Calculates the total price of {@code basket} after all discounts have been
	 * applied.
	 * 
	 * @param basket
	 *            The ShoppingBasket whose total price will be calculated.
	 * @return The ShoppingBasket whose price was requested.
	 */
	private void getTotalPriceWithDiscounts(ShoppingBasket basket) {
		checkIfBasketIsNull(basket);
		double sum = 0;
		for (Map.Entry<String, List<Product>> entry : basket.getProductsMap().entrySet()) {
			List<Product> products = entry.getValue();
			sum += products.stream().mapToDouble(product -> {
				return product.getPricePerUnit() - (product.getPricePerUnit() * product.getDiscountPercentage()); // the price of product with discount(if any)
			}).sum();
		}
		double totalPrice = sum - (sum * basket.getTotalBillDiscountPercentage()); // the price of basket with discount(if any)
		basket.setTotalPriceWithDiscounts(totalPrice);
	}

	@Override
	public void addProduct(ShoppingBasket basket, Product product) {
		checkIfBasketOrProductIsNull(basket, product);
		List<Product> productList = basket.getProductsMap().get(product.getPrimaryKey());

		if (productList == null) {// if productList is null, map did not contain that product, creating a new list
			productList = new LinkedList<>();
		}
		productList.add(product.clone()); // we want each product in list to be independent, changes on one product should not affect other products
		basket.getProductCountMap().put(product, productList.size());
		basket.getProductsMap().put(product.getPrimaryKey(), productList);
	}

	@Override
	public void addProducts(ShoppingBasket basket, Product product, int quantity) {
		checkIfBasketOrProductIsNull(basket, product);
		if (quantity <= 0) {
			throw new AppException("Quantity " + quantity + " cannot be less than 1");
		}
		List<Product> productList = basket.getProductsMap().get(product.getPrimaryKey());
		if (productList == null) { // if productList is null, map did not contain that product, creating a new list
			productList = new LinkedList<>();
		}
		for (int i = 0; i < quantity; i++) {
			productList.add(product.clone()); // we want each product in list to be independent, changes on one product should not affect other products
		}
		basket.getProductCountMap().put(product, productList.size());
		basket.getProductsMap().put(product.getPrimaryKey(), productList);
	}

	@Override
	public void updateProduct(ShoppingBasket basket, Product product) {
		checkIfBasketOrProductIsNull(basket, product);
		List<Product> productList = basket.getProductsMap().get(product.getPrimaryKey());
		if (!CollectionUtils.isEmpty(productList)) { // if productList is not null or empty, we can update the last product from productList
			productList.set(productList.size() - 1, product); // updating the last product in productList
			basket.getProductsMap().put(product.getPrimaryKey(), productList);
		} else { // if productList is null, the map doesn't contain that product
			throw new AppException("No product found in basket for product " + product);
		}
	}

	@Override
	public void removeProduct(ShoppingBasket basket, Product product) {
		checkIfBasketOrProductIsNull(basket, product);
		List<Product> productList = basket.getProductsMap().get(product.getPrimaryKey());
		if (!CollectionUtils.isEmpty(productList)) { // if productList is not null or empty, we can remove one product from productList
			productList.remove(0);
			if (productList.isEmpty()) {// productList empty, we can remove entry from map
				basket.getProductsMap().remove(product.getPrimaryKey());
			}
		} else { // if productList is null, the map doesn't contain that product
			throw new AppException("Cannot remove product, no product found in basket for product " + product);
		}
	}

	@Override
	public void removeProducts(ShoppingBasket basket, Product product, int quantity) {
		checkIfBasketOrProductIsNull(basket, product);
		if (quantity <= 0) {
			throw new AppException("Quantity " + quantity + " cannot be less than 1");
		}
		List<Product> productList = basket.getProductsMap().get(product.getPrimaryKey());
		if (!CollectionUtils.isEmpty(productList)) { // if productList is not null or empty, we can remove products from productList
			if (productList.size() <= quantity) { // if productList size is less or equal to quantity of products to remove, than we can remove the entry in map that holds that list
				basket.getProductsMap().remove(product.getPrimaryKey());
			} else { // if productList size is greater than quantity of products to remove, than we need to remove the specified number of products from list
				basket.getProductsMap().put(product.getPrimaryKey(), productList.subList(0, productList.size() - quantity));
			}
		} else {
			// if productList is null, the map doesn't contain that product
			throw new AppException("Cannot remove products, no product found in basket for product " + product);
		}

	}

	@Override
	public void removeAllSameProducts(ShoppingBasket basket, Product product) {
		checkIfBasketOrProductIsNull(basket, product);
		List<Product> productList = basket.getProductsMap().get(product.getPrimaryKey());
		if (!CollectionUtils.isEmpty(productList)) { // if productList is not null or empty, we can remove the map entry that holds that list
			basket.getProductsMap().remove(product.getPrimaryKey());
		} else {
			// if productList is null, the map doesn't contain that product
			throw new AppException("Cannot remove products, no product found in basket for product " + product);
		}
	}

	/**
	 * Checks if basket or product is null. Throws exception in case any of them is
	 * null.
	 * 
	 * @param basket
	 *            The basket to be checked.
	 * @param product
	 *            The product to be checked.
	 */
	private void checkIfBasketOrProductIsNull(ShoppingBasket basket, Product product) {
		checkIfBasketIsNull(basket);
		if (product == null) {
			throw new AppException("Supplied product is null");
		}
	}

	/**
	 * Checks if basket is null. Throws exception in case of null.
	 * 
	 * @param basket
	 *            The ShoppingBasket to be checked.
	 */
	private void checkIfBasketIsNull(ShoppingBasket basket) {
		if (basket == null) {
			throw new AppException("Supplied basket is null");
		}
	}

	@Override
	public ShoppingBasket findByUser(User user) {
		Optional<ShoppingBasket> basketByUser = basketRepository.findByUser(user);
		ShoppingBasket basket = null;
		if (!basketByUser.isPresent()) {
			basket = new ShoppingBasket();
			basket.setUser(user);
		} else {
			basket = basketByUser.get();
		}

		for (Map.Entry<Product, Integer> entry : basket.getProductCountMap().entrySet()) {
			Product product = entry.getKey();
			Integer quantity = entry.getValue();
			addProducts(basket, product, quantity);
		}

		return basket;
	}

	@Override
	public ShoppingBasket save(ShoppingBasket basket) {
		checkIfBasketIsNull(basket);
		getTotalPriceWithoutDiscounts(basket);
		calculateTotalPriceWithDiscounts(basket);
		return basketRepository.save(basket);
	}

	@Override
	public void addDiscount(ShoppingBasket basket, Discount discount) {
		checkIfBasketIsNull(basket);
		basket.getDiscounts().add(discount);
	}

	@Override
	public void empty(ShoppingBasket basket) {
		checkIfBasketIsNull(basket);
		basket.getDiscounts().clear();
		basket.getProductCountMap().clear();
		basket.getProductsMap().clear();
		basket.setTotalBillDiscountPercentage(0);
		basket.setTotalPriceWithDiscounts(0);
		basket.setTotalPriceWithoutDiscounts(0);
	}

}
