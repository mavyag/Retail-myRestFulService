package com.myretailservice.products.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.myretailservice.products.vo.Products_Name;

public interface IProductsNameRepo extends MongoRepository<Products_Name, String> {
	Products_Name findByProdid(@Param("prodid") String id);
}
