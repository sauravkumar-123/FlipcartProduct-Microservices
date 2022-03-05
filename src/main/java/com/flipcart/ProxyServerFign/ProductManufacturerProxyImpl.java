package com.flipcart.ProxyServerFign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.flipcart.Response.ProductManufacturerResponse;

public class ProductManufacturerProxyImpl implements ProductManufacturerProxy {

	private static final Logger logger=LoggerFactory.getLogger(ProductManufacturerProxyImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public ProductManufacturerResponse getProductDetailsFromManufacturer() {
	    final String url="http://ProductManufacturer/v1/productapi/get-all-productdetails"; 
	    ProductManufacturerResponse ManufacturerResponse=restTemplate.getForObject(url, ProductManufacturerResponse.class);
	    logger.info("<-----ManufacturerResponse--------->"+ManufacturerResponse);
		return ManufacturerResponse;
	}

}
