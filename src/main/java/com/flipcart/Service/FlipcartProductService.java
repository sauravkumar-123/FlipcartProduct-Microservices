package com.flipcart.Service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.flipcart.Model.FlipcartProduct;

@Service
public interface FlipcartProductService {

	public List<FlipcartProduct> addProductFromProductmanufacturer(List<Map<String, Object>> list);

	public FlipcartProduct addFlipcartProductDetails(FlipcartProduct productDetailsRequest);

	public List<FlipcartProduct> getAllFlipcartProduct();

	public List<FlipcartProduct> getFlipcartProductBySerachKey(String searchKey);

	public FlipcartProduct getFlipcartProductByProductCode(String productCode);

	public FlipcartProduct updateFlipcartProductDetailsByProductcode(String productcode,
			FlipcartProduct productDetailsRequest);

	public boolean deleteFlipcartProductByProductcode(String productcode);
}
