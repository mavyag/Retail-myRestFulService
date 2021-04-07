package com.myretailservice.products;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.myretailservice.products.rest.ProductsController;
import com.myretailservice.products.service.ProductsService;
import com.myretailservice.products.vo.Product;
import com.myretailservice.products.vo.Products_Name;
import com.myretailservice.products.vo.Products_Price;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductsController.class)
@ContextConfiguration(classes = { ProductsController.class, ProductsService.class })
class ProductsControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductsService productsService;

	@InjectMocks
	private ProductsController prodController;

	@Test
	public void fetchProductById() throws Exception {
		Map<String, Object> priceObj = new HashMap<String, Object>();
		Map<String, Object> prodObj = new HashMap<String, Object>();
		priceObj.put("value", "13.25");
		priceObj.put("currency_code", "USD");
		prodObj.put("id", "13860428");
		prodObj.put("name", "The Big Lebowski (Blu-ray)");
		prodObj.put("current_price", priceObj);
		Mockito.when(productsService.fetchProductDetails(Mockito.any(String.class))).thenReturn(prodObj);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/13860428")
				.accept(MediaType.APPLICATION_JSON);
		try {
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			System.out.println("JSON Response---->" + result.getResponse().getContentAsString());
			String expected = "{\"name\":\"The Big Lebowski (Blu-ray)\",\"id\":\"13860428\",\"current_price\":{\"value\":\"13.25\",\"currency_code\":\"USD\"}}";
			JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateProductPriceDetails() throws Exception {

		String mockProdJson = "{\n" + "    \"name\": \"Creamy Peanut Butter 40oz - Good &#38; Gather&#8482;\",\n"
				+ "    \"id\": \"54456119\",\n" + "    \"current_price\": {\n" + "        \"value\": \"14.95\",\n"
				+ "        \"currency_code\": \"EUR\"\n" + "    }\n" + "}";
		// String mockProdJson = "{\"prodid\": \"54456119\", \"name\": \"Creamy Peanut
		// Butter 40oz - Good &#38; Gather&#8482;\", \"price\": \"14.25\",
		// \"currency_code\": \"EUR\"}";
		Mockito.when(productsService.updateProductPriceDetails(Mockito.any(Product.class))).thenReturn(true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/products/54456119")
				.accept(MediaType.APPLICATION_JSON).content(mockProdJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

	@Test
	public void updateProductPriceDetails_testvalidation() throws Exception {

		String mockProdJson = "{\n" + "    \"name\": \"Creamy Peanut Butter 40oz - Good &#38; Gather&#8482;\",\n"
				+ "    \"id\": \"54456118\",\n" + "    \"current_price\": {\n" + "        \"value\": \"14.95\",\n"
				+ "        \"currency_code\": \"EUR\"\n" + "    }\n" + "}";
		// String mockProdJson = "{\"prodid\": \"54456119\", \"name\": \"Creamy Peanut
		// Butter 40oz - Good &#38; Gather&#8482;\", \"price\": \"14.25\",
		// \"currency_code\": \"EUR\"}";
		Mockito.when(productsService.updateProductPriceDetails(Mockito.any(Product.class))).thenReturn(true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/products/54456119")
				.accept(MediaType.APPLICATION_JSON).content(mockProdJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

	}

	@Test
	public void fetchPriceById() throws Exception {
		Products_Price prodPrice = new Products_Price();
		prodPrice.setProdid("13860428");
		prodPrice.setPrice("13.25");
		prodPrice.setCurrency_code("USD");
		Mockito.when(productsService.fetchProductPrice(Mockito.any(Products_Price.class))).thenReturn(prodPrice);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/13860428/price")
				.accept(MediaType.APPLICATION_JSON);
		try {
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			System.out.println("JSON Response---->" + result.getResponse().getContentAsString());
			String expected = "{\"id\":\"13860428\",\"value\":\"13.25\",\"currency_code\":\"USD\"}";
			JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void fetchNameById() throws Exception {
		Products_Name prodName = new Products_Name();
		prodName.setProdid("13860428");
		prodName.setName("The Big Lebowski (Blu-ray)");
		Mockito.when(productsService.fetchProductName(Mockito.any(Products_Name.class))).thenReturn(prodName);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/13860428/name").accept(MediaType.APPLICATION_JSON);
		try {
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			System.out.println("JSON Response---->" + result.getResponse().getContentAsString());
			String expected = "{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\"}";
			JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
