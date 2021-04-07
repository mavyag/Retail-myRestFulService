package com.myretailservice.products.dao;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
public class MongoAppConfig extends AbstractMongoClientConfiguration {
	@Override
	protected String getDatabaseName() {
		return "products_details";
	}

}
