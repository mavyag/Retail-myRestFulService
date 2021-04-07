package com.myretailservice.products;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.myretailservice.products.app.ProductsApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductsApplication.class)
class ProductsApplicationTests {
	@Test
	public void contextLoads() {
	}
}
