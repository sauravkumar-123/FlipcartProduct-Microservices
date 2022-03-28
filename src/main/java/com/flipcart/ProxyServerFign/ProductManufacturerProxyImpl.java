package com.flipcart.ProxyServerFign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.flipcart.Response.ProductManufacturerResponse;

public class ProductManufacturerProxyImpl implements ProductManufacturerProxy {

	private static final Logger logger = LoggerFactory.getLogger(ProductManufacturerProxyImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public ProductManufacturerResponse getProductDetailsFromManufacturer() {
		final String URL = "http://ProductManufacturer/v1/productmanufacture/get-all-productdetails";
		ProductManufacturerResponse productManufacturerResponse = restTemplate.getForObject(URL,ProductManufacturerResponse.class);
		logger.info("Responce From Manufacturer:{}" + productManufacturerResponse);
		return productManufacturerResponse;
	}

}
