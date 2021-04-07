package com.myretailservice.products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;

@ControllerAdvice
public class ProductExceptionController {
	@ExceptionHandler(value = ProductNotFoundException.class)
	public ResponseEntity<Object> exception(ProductNotFoundException exception) {
		return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = JsonProcessingException.class)
	public ResponseEntity<Object> exception(JsonProcessingException exception) {
		return new ResponseEntity<>("Error in JSON", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = RestClientException.class)
	public ResponseEntity<Object> exception(RestClientException exception) {
		return new ResponseEntity<>("Product not found in external API", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> exception(Exception exception) {
		return new ResponseEntity<>("Exception occurred in Products REST API", HttpStatus.NOT_FOUND);
	}
}
