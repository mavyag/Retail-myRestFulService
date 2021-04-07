package com.myretailservice.products.service;

import java.util.Map;

import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myretailservice.products.exception.ProductNotFoundException;
import com.myretailservice.products.vo.Product;
import com.myretailservice.products.vo.Products_Name;
import com.myretailservice.products.vo.Products_Price;

public interface IProductsService {

	Map<String, Object> fetchProductDetails(String id)
			throws JsonProcessingException, ProductNotFoundException, RestClientException;

	Products_Price fetchProductPrice(Products_Price product) throws JsonProcessingException;

	Products_Name fetchProductName(Products_Name product) throws JsonProcessingException;

	boolean updateProductPriceDetails(Product product);

}
