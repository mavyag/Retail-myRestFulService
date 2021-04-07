package com.myretailservice.products.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myretailservice.products.exception.ProductNotFoundException;
import com.myretailservice.products.service.IProductsService;
import com.myretailservice.products.vo.Product;
import com.myretailservice.products.vo.Products_Name;
import com.myretailservice.products.vo.Products_Price;

@RestController
public class ProductsController {

	private static final String ID_MISMATCH_MSG = "Ids are not matching. Please check.";
	private static final String PRICE_MISSING_MSG = "Price is empty. Please check";
	private static final String PROD_UPDATE_MSG = "Product is updated successfully";
	private static final String PRICE_VALUE_MSG = "Price should contain only numbers. Please check";

	@Autowired
	private IProductsService productsService;

	@GetMapping(value = "/products/{id}")
	public Map<String, Object> fetchProductById(@PathVariable String id)
			throws JsonProcessingException, ProductNotFoundException, RestClientException {
		Map<String, Object> productMap = new HashMap<String, Object>();
		if(!id.matches("[0-9]*")) {
			productMap.put("error", "Product Id should be numeric only.");
		}
		else {
			productMap = productsService.fetchProductDetails(id);
		}
		return productMap;
	}

	@GetMapping(value = "/{productId}/price")
	public Map<String, Object> fetchPriceById(@PathVariable String productId)
			throws JsonProcessingException, ProductNotFoundException, RestClientException {
		Products_Price product = new Products_Price();
		product.setProdid(productId);
		product = productsService.fetchProductPrice(product);// Get External Product
		Map<String, Object> priceObj = new HashMap<String, Object>();
		priceObj.put("id", product.getProdid());
		priceObj.put("value", product.getPrice());
		priceObj.put("currency_code", product.getCurrency_code());
		return priceObj;

	}

	@GetMapping(value = "/{productId}/name")
	public Map<String, Object> fetchNameById(@PathVariable String productId)
			throws JsonProcessingException, ProductNotFoundException, RestClientException {
		Products_Name product = new Products_Name();
		product.setProdid(productId);
		product = productsService.fetchProductName(product);
		Map<String, Object> prodObj = new HashMap<String, Object>();
		prodObj.put("id", product.getProdid());
		prodObj.put("name", product.getName());
		return prodObj;
	}

	@RequestMapping(method = { RequestMethod.PUT, RequestMethod.POST }, value = "/products/{id}")
	public ResponseEntity<Object> updateProductPriceDetails(@RequestBody Product product, @PathVariable String id)
			throws ProductNotFoundException {
		ResponseEntity<Object> responseEntity = validateProduct(product, id);
		if (null == responseEntity) {
			if (productsService.updateProductPriceDetails(product)) {
				responseEntity = new ResponseEntity<>(PROD_UPDATE_MSG, HttpStatus.OK);
			}
		}
		return responseEntity;
	}

	private ResponseEntity<Object> validateProduct(Product product, String id) {
		ResponseEntity<Object> responseEntity = null;
		if (!product.getId().equals(id)) { // If IDs don't match
			responseEntity = new ResponseEntity<>(ID_MISMATCH_MSG, HttpStatus.NOT_FOUND);
		} else if (StringUtils.isEmpty(product.getCurrent_price().get("value"))) { // If price is empty
			responseEntity = new ResponseEntity<>(PRICE_MISSING_MSG, HttpStatus.NOT_FOUND);
		}
		else if (!product.getCurrent_price().get("value").matches("[0-9.]*")) { // If price is empty
			responseEntity = new ResponseEntity<>(PRICE_VALUE_MSG, HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}

}
