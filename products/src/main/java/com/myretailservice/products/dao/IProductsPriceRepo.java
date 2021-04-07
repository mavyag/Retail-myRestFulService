package com.myretailservice.products.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.myretailservice.products.vo.Products_Price;

public interface IProductsPriceRepo extends MongoRepository<Products_Price, String> {
	Products_Price findByProdid(@Param("prodid") String id);
}
