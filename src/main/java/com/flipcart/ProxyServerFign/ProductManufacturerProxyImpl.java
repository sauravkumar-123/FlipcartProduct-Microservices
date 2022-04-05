package com.flipcart.ProxyServerFign;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.flipcart.ExceptionHandle.GlobalException;
import com.flipcart.Response.ProductManufacturerResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;

public class ProductManufacturerProxyImpl implements ProductManufacturerProxy {

	private static final Logger logger = LoggerFactory.getLogger(ProductManufacturerProxyImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Override
	@HystrixCommand(commandKey = "flipcartProduct", groupKey = "flipcartProduct", fallbackMethod = "getProductDetailsFromManufacturerFallback")
	public ProductManufacturerResponse getProductDetailsFromManufacturer() {
		final String URL = "http://ProductManufacturer/v1/productmanufacture/get-all-productdetails";
		try {
			ProductManufacturerResponse productManufacturerResponse = restTemplate.getForObject(URL,
					ProductManufacturerResponse.class);
			logger.info("Responce From Manufacturer:{}" + productManufacturerResponse);
			return productManufacturerResponse;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Server Error!!While Calling ProductManufacturer");
		}
	}

	public ProductManufacturerResponse getProductDetailsFromManufacturerFallback() {
		ProductManufacturerResponse maufacturerResponse = new ProductManufacturerResponse(ZonedDateTime.now(), false,
				"ProductManufacturer Service Gateway Not Respond!!Try Again After Some Time", null);
		return maufacturerResponse;
	}
}
