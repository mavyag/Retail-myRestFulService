package com.myretailservice.products.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretailservice.products.dao.IProductsNameRepo;
import com.myretailservice.products.dao.IProductsPriceRepo;
import com.myretailservice.products.exception.ProductNotFoundException;
import com.myretailservice.products.vo.Product;
import com.myretailservice.products.vo.Products_Name;
import com.myretailservice.products.vo.Products_Price;

@Service
public class ProductsService implements IProductsService {

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private IProductsPriceRepo productsRepo;

	@Autowired
	private IProductsNameRepo prodNameRepo;

	@Autowired
	private RestTemplate restTemplate;

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	public IProductsPriceRepo getProductsRepo() {
		return productsRepo;
	}

	public void setProductsRepo(IProductsPriceRepo productsRepo) {
		this.productsRepo = productsRepo;
	}

	public IProductsNameRepo getProdNameRepo() {
		return prodNameRepo;
	}

	public void setProdNameRepo(IProductsNameRepo prodNameRepo) {
		this.prodNameRepo = prodNameRepo;
	}

	public Products_Price fetchProductPrice(Products_Price product)
			throws JsonProcessingException, ProductNotFoundException, RestClientException {
		Products_Price productDB = productsRepo.findByProdid(product.getProdid());
		if (null == productDB) {
			throw new ProductNotFoundException();
		}
		return productDB;
	}

	public Products_Name fetchProductName(Products_Name product)
			throws JsonProcessingException, ProductNotFoundException, RestClientException {
		Products_Name productDB = prodNameRepo.findByProdid(product.getProdid());
		if (null == productDB) {
			throw new ProductNotFoundException();
		}
		return productDB;
	}

	public Map<String, Object> fetchProductDetails(String id) throws ProductNotFoundException, RestClientException {
		Map<String, Object> priceObj = new HashMap<String, Object>();
		Map<String, Object> prodObj = new HashMap<String, Object>();
		try {
			String prodPriceUrl = appConfig.getProdPriceUrl();
			prodPriceUrl = prodPriceUrl.replace("id", id);
			Map<String, String> priceMap = (Map<String, String>) makeRestCall(prodPriceUrl);
			priceObj.put("value", priceMap.get("value"));
			priceObj.put("currency_code", priceMap.get("currency_code"));
			String prodNameUrl = appConfig.getProdNameUrl();
			prodNameUrl = prodNameUrl.replace("id", id);
			Map<String, String> nameMap = (Map<String, String>) makeRestCall(prodNameUrl);
			prodObj.put("id", id);
			prodObj.put("name", nameMap.get("name"));
			prodObj.put("current_price", priceObj);

		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return prodObj;
	}

	public Map makeRestCall(String url) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map> map = new HashMap<String, Map>();
		ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
		HttpStatus responseCode = response.getStatusCode();
		if (responseCode == HttpStatus.OK) {
			map = response.getBody();
		}
		return map;
	}

	public boolean updateProductPriceDetails(Product product) {
		Products_Price productDB = productsRepo.findByProdid(product.getId());
		if (productDB != null) {
			productDB.setPrice(product.getCurrent_price().get("value"));
			productsRepo.save(productDB);

		} else {
			throw new ProductNotFoundException();
		}
		return true;
	}

}
