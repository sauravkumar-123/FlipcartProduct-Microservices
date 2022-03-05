package com.flipcart.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.flipcart.Request.ProductDetailsRequest;

@Service
public interface FlipcartProductService {
	public String addProductFromManufacturer(List<Map<String, Object>> list);
	public String addProductDetails(ProductDetailsRequest productDetailsRequest) throws ParseException;
	public List<Map<String, Object>> getAllProduct();
	public  List<Map<String, Object>> getProductByProductcode(String productcode);
	public String updateProductDetailsByProductcode(String productcode,ProductDetailsRequest productDetailsRequest);
	public String deleteProductByProductcode(String productcode);
}
