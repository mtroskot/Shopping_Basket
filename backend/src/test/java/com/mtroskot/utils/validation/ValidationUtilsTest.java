package com.mtroskot.utils.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.mtroskot.exception.AppException;
import com.mtroskot.model.entity.discount.FreeProductDiscount;
import com.mtroskot.model.entity.product.Bread;
import com.mtroskot.model.entity.product.ProductCategory;
import com.mtroskot.model.entity.product.ShoppingBasket;

@RunWith(SpringRunner.class)
public class ValidationUtilsTest {

	private final String DISCOUNT_MESSAGE_1 = "Discount percentage cannot be less than 0";
	private final String DISCOUNT_MESSAGE_2 = "Discount percentage cannot be greater than 1";
	private final String PRODUCT_AMOUNT = "Amount of products cannot be less than 1";
	private final String PRODUCT = "Product cannot be null";
	private final String DISCOUNT = "Discount cannot be null";
	private final String BASKET = "Basket cannot be null";
	private final String CATEGORY = "Product category cannot be null";
	private final String PRICE = "Price cannot be less than 0";
	private final String NAME_1 = "Name cannot be null";
	private final String NAME_2 = "Name cannot be blank";

	@Test
	public void emailRegexTest() {
		String email1 = "marko.troskot";
		String email2 = "marko.com";
		String email3 = "marko@@hotmail.com";

		String email4 = "marko@hotmail.co.uk";
		String email5 = "marko.troskot@hotmail.co.uk";
		String email6 = "MarkO.TroskoT@HOTMAIL.co.uk";
		String email7 = "markoć@hotmail.com";
		String email8 = "marko@hotmail";

		Assert.assertEquals("email1 matches email pattern - false", false, ValidationUtils.PATTERN_EMAIL.matcher(email1).matches());
		Assert.assertEquals("email2 matches email pattern - false", false, ValidationUtils.PATTERN_EMAIL.matcher(email2).matches());
		Assert.assertEquals("email3 matches email pattern - false", false, ValidationUtils.PATTERN_EMAIL.matcher(email3).matches());

		Assert.assertEquals("email4 matches email pattern - true", true, ValidationUtils.PATTERN_EMAIL.matcher(email4).matches());
		Assert.assertEquals("email5 matches email pattern - true", true, ValidationUtils.PATTERN_EMAIL.matcher(email5).matches());
		Assert.assertEquals("email6 matches email pattern - true", true, ValidationUtils.PATTERN_EMAIL.matcher(email6).matches());
		Assert.assertEquals("email7 matches email pattern - true", true, ValidationUtils.PATTERN_EMAIL.matcher(email7).matches());
		Assert.assertEquals("email8 matches email pattern - true", true, ValidationUtils.PATTERN_EMAIL.matcher(email8).matches());
	}

	@Test
	public void firstNameRegexTest() {
		String firstName1 = "M";
		String firstName2 = "MarkO";
		String firstName3 = "Marko Luka";
		String firstName4 = "Marko1";
		String firstName5 = "Marko ";
		String firstName6 = " Marko";
		String firstName7 = "đuro";
		String firstName8 = "Markoooooooooooooooooo";
		String firstName9 = "";

		String firstName10 = "Ma";
		String firstName11 = "Marko";
		String firstName12 = "Đuro";
		String firstName13 = "Markooooooooooooooooo";

		Assert.assertEquals("firstName1 matches firstName pattern - false", false, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName1).matches());
		Assert.assertEquals("firstName2 matches firstName pattern - false", false, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName2).matches());
		Assert.assertEquals("firstName3 matches firstName pattern - false", false, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName3).matches());
		Assert.assertEquals("firstName4 matches firstName pattern - false", false, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName4).matches());
		Assert.assertEquals("firstName5 matches firstName pattern - false", false, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName5).matches());
		Assert.assertEquals("firstName6 matches firstName pattern - false", false, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName6).matches());
		Assert.assertEquals("firstName7 matches firstName pattern - false", false, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName7).matches());
		Assert.assertEquals("firstName8 matches firstName pattern - false", false, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName8).matches());
		Assert.assertEquals("firstName9 matches firstName pattern - false", false, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName9).matches());

		Assert.assertEquals("firstName10 matches firstName pattern - true", true, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName10).matches());
		Assert.assertEquals("firstName11 matches firstName pattern - true", true, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName11).matches());
		Assert.assertEquals("firstName12 matches firstName pattern - true", true, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName12).matches());
		Assert.assertEquals("firstName13 matches firstName pattern - true", true, ValidationUtils.PATTERN_FIRST_NAME.matcher(firstName13).matches());
	}

	@Test
	public void lastNameRegexTest() {
		String lastName1 = "T";
		String lastName2 = "TroskoT";
		String lastName3 = "";
		String lastName4 = " Tro skot ";
		String lastName5 = " Troskot";
		String lastName6 = "Troskot ";
		String lastName7 = "Troskotttttttttttttttt";

		String lastName8 = "Tr";
		String lastName9 = "Troskottttttttttttttt";
		String lastName10 = "Troskotž";

		Assert.assertEquals("lastName1 matches lastName pattern - false", false, ValidationUtils.PATTERN_LAST_NAME.matcher(lastName1).matches());
		Assert.assertEquals("lastName2 matches lastName pattern - false", false, ValidationUtils.PATTERN_LAST_NAME.matcher(lastName2).matches());
		Assert.assertEquals("lastName3 matches lastName pattern - false", false, ValidationUtils.PATTERN_LAST_NAME.matcher(lastName3).matches());
		Assert.assertEquals("lastName4 matches lastName pattern - false", false, ValidationUtils.PATTERN_LAST_NAME.matcher(lastName4).matches());
		Assert.assertEquals("lastName5 matches lastName pattern - false", false, ValidationUtils.PATTERN_LAST_NAME.matcher(lastName5).matches());
		Assert.assertEquals("lastName6 matches lastName pattern - false", false, ValidationUtils.PATTERN_LAST_NAME.matcher(lastName6).matches());
		Assert.assertEquals("lastName7 matches lastName pattern - false", false, ValidationUtils.PATTERN_LAST_NAME.matcher(lastName7).matches());

		Assert.assertEquals("lastName8 matches lastName pattern - true", true, ValidationUtils.PATTERN_LAST_NAME.matcher(lastName8).matches());
		Assert.assertEquals("lastName9 matches lastName pattern - true", true, ValidationUtils.PATTERN_LAST_NAME.matcher(lastName9).matches());
		Assert.assertEquals("lastName10 matches lastName pattern - true", true, ValidationUtils.PATTERN_LAST_NAME.matcher(lastName10).matches());
	}

	@Test
	public void usernameRegexTest() {
		String username1 = "";
		String username2 = " mtro";
		String username3 = " mtro ";
		String username4 = "mtr o";
		String username5 = "mt";
		String username5a = "mtroskotttttttttttttt";

		String username6 = "mtro1234";
		String username7 = "MTRO1234";
		String username8 = "mTro12";
		String username9 = "Mtro12";
		String username10 = "mtrO12";
		String username11 = "mtroskot";

		Assert.assertEquals("username1 matches username pattern - false", false, ValidationUtils.PATTERN_USERNAME.matcher(username1).matches());
		Assert.assertEquals("username2 matches username pattern - false", false, ValidationUtils.PATTERN_USERNAME.matcher(username2).matches());
		Assert.assertEquals("username3 matches username pattern - false", false, ValidationUtils.PATTERN_USERNAME.matcher(username3).matches());
		Assert.assertEquals("username4 matches username pattern - false", false, ValidationUtils.PATTERN_USERNAME.matcher(username4).matches());
		Assert.assertEquals("username5 matches username pattern - false", false, ValidationUtils.PATTERN_USERNAME.matcher(username5).matches());
		Assert.assertEquals("username5a matches username pattern - false", false, ValidationUtils.PATTERN_USERNAME.matcher(username5a).matches());

		Assert.assertEquals("username6 matches username pattern - true", true, ValidationUtils.PATTERN_USERNAME.matcher(username6).matches());
		Assert.assertEquals("username7 matches username pattern - true", true, ValidationUtils.PATTERN_USERNAME.matcher(username7).matches());
		Assert.assertEquals("username8 matches username pattern - true", true, ValidationUtils.PATTERN_USERNAME.matcher(username8).matches());
		Assert.assertEquals("username9 matches username pattern - true", true, ValidationUtils.PATTERN_USERNAME.matcher(username9).matches());
		Assert.assertEquals("username10 matches username pattern - true", true, ValidationUtils.PATTERN_USERNAME.matcher(username10).matches());
		Assert.assertEquals("username11 matches username pattern - true", true, ValidationUtils.PATTERN_USERNAME.matcher(username11).matches());
	}

	@Test
	public void passwordRegexTest() {
		String password1 = "";
		String password2 = "mtroskot";
		String password3 = " Mtros4";
		String password4 = "Mtros4 ";
		String password5 = "mTrosko t4";
		String password6 = "mTroskot4tttttttttttt";
		String password7 = "mtroskot4";
		String password8 = "MTROSKOT3";
		String password9 = "123456";

		String password10 = "mTro321";
		String password11 = "MTRo123";
		String password12 = "mtrO123";

		Assert.assertEquals("password1 matches password pattern - false", false, ValidationUtils.PATTERN_PASSWORD.matcher(password1).matches());
		Assert.assertEquals("password2 matches password pattern - false", false, ValidationUtils.PATTERN_PASSWORD.matcher(password2).matches());
		Assert.assertEquals("password3 matches password pattern - false", false, ValidationUtils.PATTERN_PASSWORD.matcher(password3).matches());
		Assert.assertEquals("password4 matches password pattern - false", false, ValidationUtils.PATTERN_PASSWORD.matcher(password4).matches());
		Assert.assertEquals("password5 matches password pattern - false", false, ValidationUtils.PATTERN_PASSWORD.matcher(password5).matches());
		Assert.assertEquals("password6 matches password pattern - false", false, ValidationUtils.PATTERN_PASSWORD.matcher(password6).matches());
		Assert.assertEquals("password7 matches password pattern - false", false, ValidationUtils.PATTERN_PASSWORD.matcher(password7).matches());
		Assert.assertEquals("password8 matches password pattern - false", false, ValidationUtils.PATTERN_PASSWORD.matcher(password8).matches());
		Assert.assertEquals("password9 matches password pattern - false", false, ValidationUtils.PATTERN_PASSWORD.matcher(password9).matches());

		Assert.assertEquals("password10 matches password pattern - true", true, ValidationUtils.PATTERN_PASSWORD.matcher(password10).matches());
		Assert.assertEquals("password11 matches password pattern - true", true, ValidationUtils.PATTERN_PASSWORD.matcher(password11).matches());
		Assert.assertEquals("password12 matches password pattern - true", true, ValidationUtils.PATTERN_PASSWORD.matcher(password12).matches());
	}

	@Test
	public void validateTest() {
		Assert.assertEquals("Validate should return false", false, ValidationUtils.validate(null, ValidationUtils.PATTERN_EMAIL));
		Assert.assertEquals("Validate should return false", false, ValidationUtils.validate("marko.com", ValidationUtils.PATTERN_EMAIL));
		Assert.assertEquals("Validate should return true", true, ValidationUtils.validate("marko@gotmail.com", ValidationUtils.PATTERN_EMAIL));
	}

	@Test
	public void validateDiscountPercentageTest1() {
		try {
			ValidationUtils.validateDiscountPercentage(-0.01);
		} catch (AppException e) {
			assertEquals("Message should be DISCOUNT_MESSAGE_1", DISCOUNT_MESSAGE_1, e.getMessage());
		}
		try {
			ValidationUtils.validateDiscountPercentage(1.01);
		} catch (AppException e) {
			assertEquals("Message should be DISCOUNT_MESSAGE_2", DISCOUNT_MESSAGE_2, e.getMessage());
		}
	}

	@Test
	public void validateDiscountPercentageTest2() {
		ValidationUtils.validateDiscountPercentage(1);
		ValidationUtils.validateDiscountPercentage(0);
		ValidationUtils.validateDiscountPercentage(0.5);
		ValidationUtils.validateDiscountPercentage(0.9);
	}

	@Test
	public void validateProductDiscountAmountTest1() {
		try {
			ValidationUtils.validateProductDiscountAmount(-1);
		} catch (AppException e) {
			assertEquals("Message should be PRODUCT_AMOUNT", PRODUCT_AMOUNT, e.getMessage());
		}
		try {
			ValidationUtils.validateDiscountPercentage(0);
		} catch (AppException e) {
			assertEquals("Message should be PRODUCT_AMOUNT", PRODUCT_AMOUNT, e.getMessage());
		}
	}

	@Test
	public void validateProductDiscountAmountTest2() {
		ValidationUtils.validateProductDiscountAmount(1);
		ValidationUtils.validateProductDiscountAmount(5);
		ValidationUtils.validateProductDiscountAmount(10);
		ValidationUtils.validateProductDiscountAmount(100);
		ValidationUtils.validateProductDiscountAmount(101);
	}

	@Test
	public void validateProductTest1() {
		try {
			ValidationUtils.validateProduct(null);
		} catch (AppException e) {
			assertEquals("Message should be PRODUCT", PRODUCT, e.getMessage());
		}
	}

	@Test
	public void validateProductTest2() {
		ValidationUtils.validateProduct(new Bread());
	}

	@Test
	public void validateDiscountTest1() {
		try {
			ValidationUtils.validateDiscount(null);
		} catch (AppException e) {
			assertEquals("Message should be DISCOUNT", DISCOUNT, e.getMessage());
		}
	}

	@Test
	public void validateDiscountTest2() {
		ValidationUtils.validateDiscount(new FreeProductDiscount());
	}

	@Test
	public void validateShoppingBasketTest1() {
		try {
			ValidationUtils.validateShoppingBasket(null);
		} catch (AppException e) {
			assertEquals("Message should be BASKET", BASKET, e.getMessage());
		}
	}

	@Test
	public void validateShoppingBasketTest2() {
		ValidationUtils.validateShoppingBasket(new ShoppingBasket());
	}

	@Test
	public void validateProductCategoryTest1() {
		try {
			ValidationUtils.validateProductCategory(null);
		} catch (AppException e) {
			assertEquals("Message should be CATEGORY", CATEGORY, e.getMessage());
		}
	}

	@Test
	public void validateProductCategoryTest2() {
		ValidationUtils.validateProductCategory(new ProductCategory());
	}

	@Test
	public void validatePriceTest1() {
		try {
			ValidationUtils.validatePrice(-0.01);
		} catch (AppException e) {
			assertEquals("Message should be PRICE", PRICE, e.getMessage());
		}
	}

	@Test
	public void validatePriceTest2() {
		ValidationUtils.validatePrice(142.2);
		ValidationUtils.validatePrice(0.5);
		ValidationUtils.validatePrice(10.5);
		ValidationUtils.validatePrice(0.9);
	}

	@Test
	public void validateNameTest1() {
		try {
			ValidationUtils.validateName(null);
		} catch (AppException e) {
			assertEquals("Message should be NAME_1", NAME_1, e.getMessage());
		}
		try {
			ValidationUtils.validateName("");
		} catch (AppException e) {
			assertEquals("Message should be NAME_2", NAME_2, e.getMessage());
		}
		try {
			ValidationUtils.validateName("     ");
		} catch (AppException e) {
			assertEquals("Message should be NAME_2", NAME_2, e.getMessage());
		}
	}

	@Test
	public void validateNameTest2() {
		ValidationUtils.validateName("Marko");
		ValidationUtils.validateName("Luka");
		ValidationUtils.validateName("Ivan");
	}

}
