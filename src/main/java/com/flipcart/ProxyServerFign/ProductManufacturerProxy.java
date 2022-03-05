package com.flipcart.ProxyServerFign;

import com.flipcart.Response.ProductManufacturerResponse;

//@FeignClient(name="${product.service.name}",url ="localhost:8080/v1/productapi")
//@FeignClient(name="${product.service.name}")
//@RibbonClient(name="${product.service.name}")
//@FeignClient(name="ProductManufacturer")
public interface ProductManufacturerProxy {

	//@GetMapping("/get-all-productdetails")
	public ProductManufacturerResponse getProductDetailsFromManufacturer();
}
