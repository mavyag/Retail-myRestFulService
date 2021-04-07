package com.myretailservice.products.vo;

import java.util.Map;

public class Product {
	public String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String name;
	public Map<String, String> current_price;

	public Map<String, String> getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(Map<String, String> current_price) {
		this.current_price = current_price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
