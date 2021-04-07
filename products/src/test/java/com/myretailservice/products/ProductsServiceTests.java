package com.myretailservice.products;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretailservice.products.dao.IProductsNameRepo;
import com.myretailservice.products.dao.IProductsPriceRepo;
import com.myretailservice.products.dao.MongoAppConfig;
import com.myretailservice.products.service.AppConfig;
import com.myretailservice.products.service.ProductsService;
import com.myretailservice.products.vo.Product;
import com.myretailservice.products.vo.Products_Name;
import com.myretailservice.products.vo.Products_Price;

@RunWith(SpringRunner.class)
@RestClientTest(ProductsService.class)
@ContextConfiguration(classes = { ProductsService.class, AppConfig.class, IProductsNameRepo.class,
		IProductsPriceRepo.class, MongoAppConfig.class })
class ProductsServiceTests {

	@Autowired
	private IProductsPriceRepo productsRepo;

	@Autowired
	private IProductsNameRepo prodNameRepo;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	RestTemplate restTemplate;

	@InjectMocks
	private AppConfig appConfig;

	@InjectMocks
	private ProductsService productService;

	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() throws Exception {
		// RestTemplate restTemplate = restTemplateBuilder.build();
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void fetchProductDetails() throws Exception {
		Map<String, Object> priceObj = new HashMap<String, Object>();
		Map<String, Object> prodObj = new HashMap<String, Object>();
		priceObj.put("value", "13.25");
		priceObj.put("currency_code", "USD");
		prodObj.put("id", "13860428");
		prodObj.put("name", "The Big Lebowski (Blu-ray)");
		prodObj.put("current_price", priceObj);
		mockServer.expect(requestTo(new URI("http://localhost:8080/13860428/price"))).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(priceObj)));
		mockServer.expect(requestTo(new URI("http://localhost:8080/13860428/name"))).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(prodObj)));

		String id = "13860428";
		appConfig.setProdPriceUrl("http://localhost:8080/id/price");
		appConfig.setProdNameUrl("http://localhost:8080/id/name");
		productService.setAppConfig(appConfig);
		productService.setProdNameRepo(prodNameRepo);
		productService.setProductsRepo(productsRepo);
		productService.setRestTemplate(restTemplate);
		// Mockito.when( restTemplate.getForEntity("http://localhost:8080/id/price",
		// Map.class)).thenReturn(priceResponse);
		// productService.setRestTemplate(restTemplate);
		Map<String, Object> actualProdObj = productService.fetchProductDetails(id);
		assertEquals(prodObj, actualProdObj);
	}

	@Test
	public void updateProductPriceDetails() throws Exception {
		Product product = new Product();
		Map<String, String> current_price = new HashMap<String, String>();
		current_price.put("value", "13.75");
		product.setId("13860428");
		product.setCurrent_price(current_price);
		Products_Price prodPrice = new Products_Price();
		prodPrice.setProdid("13860428");
		productService.setProductsRepo(productsRepo);
		boolean result = productService.updateProductPriceDetails(product);
		assertTrue(result);
	}

	@Test
	public void fetchProductPrice() throws Exception {
		Products_Price prodPrice = new Products_Price();
		prodPrice.setProdid("13860428");
		productService.setProductsRepo(productsRepo);
		prodPrice = productService.fetchProductPrice(prodPrice);
		assertNotNull(prodPrice.getPrice());
	}

	@Test
	public void fetchProductName() throws Exception {
		Products_Name product = new Products_Name();
		product.setProdid("13860428");
		productService.setProdNameRepo(prodNameRepo);
		product = productService.fetchProductName(product);
		assertNotNull(product.getName());
	}

}
