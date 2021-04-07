package com.myretailservice.products.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class AppConfig {

	@Autowired
	private Environment environment;

	@Value("${product.url.external}")
	private String externalApiUrl;

	@Value("${product.price.url}")
	private String prodPriceUrl;

	@Value("${product.name.url}")
	private String prodNameUrl;

	public String getProdNameUrl() {
		return prodNameUrl;
	}

	public void setProdNameUrl(String prodNameUrl) {
		this.prodNameUrl = prodNameUrl;
	}

	public String getProdPriceUrl() {
		return prodPriceUrl;
	}

	public void setProdPriceUrl(String prodPriceUrl) {
		this.prodPriceUrl = prodPriceUrl;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public String getExternalApiUrl() {
		return externalApiUrl;
	}

	public void setExternalApiUrl(String externalApiUrl) {
		this.externalApiUrl = externalApiUrl;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Do any additional configuration here
		return builder.build();
	}
}
