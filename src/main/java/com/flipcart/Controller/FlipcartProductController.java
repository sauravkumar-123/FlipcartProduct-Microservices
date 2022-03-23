package com.flipcart.Controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flipcart.ExceptionHandle.GlobalException;
import com.flipcart.Model.FlipcartProduct;
import com.flipcart.ProxyServerFign.ProductManufacturerProxy;
import com.flipcart.Response.FlipcartProductResponse;
import com.flipcart.Response.ProductManufacturerResponse;
import com.flipcart.Service.FlipcartProductService;

import io.swagger.annotations.Api;

@Api(value = "FlipcartProductController", description = "This is FlipcartProduct Controller for communicate with ProductManufacturer API")
@RestController
@RequestMapping(value = "/v1/flipcartproductapi")
public class FlipcartProductController {

	private static final Logger logger = LoggerFactory.getLogger(FlipcartProductController.class);

	@Autowired
	private FlipcartProductService productService;

	@Autowired
	private ProductManufacturerProxy proxyserverdata;

	@GetMapping("/get-product-from-manufacturer")
	public ResponseEntity<FlipcartProductResponse> getProductDetailsFromManufacturer() {

		ProductManufacturerResponse manufacturerResponse = proxyserverdata.getProductDetailsFromManufacturer();
		if (null != manufacturerResponse && manufacturerResponse.isStatus()) {
			try {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> productList = (List<Map<String, Object>>) manufacturerResponse
						.getDatasource();
				logger.info("<---Response from ProductManufacturer--->" + productList);
				if (null != productList && !productList.isEmpty()) {
					List<FlipcartProduct> flipcartProduct = productService
							.addProductFromProductmanufacturer(productList);
					return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(true,
							"Product Details Saved From Manufacturer", flipcartProduct), HttpStatus.CREATED);
				} else {
					throw new NullPointerException("Data Not Found from productManufacturer server");
				}
				/*******************************************************************
				 * JSONArray json_arr=new JSONArray(); JSONObject json_obj=new JSONObject(); for
				 * (Map<String, Object> map : list) { for (Map.Entry<String, Object> entry :
				 * map.entrySet()) { String key = entry.getKey(); Object value =
				 * entry.getValue(); try { json_obj.put(key,value); } catch (JSONException e) {
				 * // TODO Auto-generated catch block e.printStackTrace(); } }
				 * json_arr.put(json_obj); }
				 *********************************************************************/
			} catch (Exception e) {
				e.printStackTrace();
				throw new GlobalException("Server Error!!! while fetch data from productManufacturer server");
			}
		} else {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(false, "Data Not Found From ProductManufacturer!!Please Check", null),
					HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/add-productdetails")
	public ResponseEntity<FlipcartProductResponse> addProductDetails(
			@Valid @ModelAttribute FlipcartProduct productDetailsRequest) {
		logger.info("<---Request Payload--->" + productDetailsRequest);
		FlipcartProduct flipcartProduct = productService.addFlipcartProductDetails(productDetailsRequest);
		if (null != flipcartProduct) {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(true, "flipcart Product Detail Saved", flipcartProduct),
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(false, "Unable To Save flipcart Product Detail", flipcartProduct),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get-all-productdetails")
	public ResponseEntity<FlipcartProductResponse> getProductDetails() {
		List<FlipcartProduct> prodList = productService.getAllFlipcartProduct();
		logger.info("<-----flipcart Product List----->" + prodList);
		if (null != prodList && !prodList.isEmpty()) {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(true, "Product Details List Fetched", prodList), HttpStatus.OK);
		} else {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(false, "Unable To Fetch Product Details List", prodList),
					HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/get-productdetailsByKey")
	public ResponseEntity<FlipcartProductResponse> getProductBySearchkey(@RequestParam("searchKey") String searchKey) {
		logger.info("<---Search Key--->" + searchKey);
		List<FlipcartProduct> prodList = productService.getFlipcartProductBySerachKey(searchKey);
		logger.info("<-----Product List----->" + prodList);
		if (null != prodList && !prodList.isEmpty()) {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(true, "Product Details Fetched", prodList), HttpStatus.OK);
		} else {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(false, "Unable To Fetch Product Details", prodList),
					HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/update-productdetails/{productCode}")
	public ResponseEntity<FlipcartProductResponse> updateProductdetailsByProductcode(
			@PathVariable("productCode") String productCode,
			@Valid @ModelAttribute FlipcartProduct productDetailsRequest) {

		logger.info("<---ProductCode And Request Payload--->" + productCode + " " + productDetailsRequest);
		FlipcartProduct product = productService.updateFlipcartProductDetailsByProductcode(productCode,
				productDetailsRequest);
		logger.info("<-----Updated Product Detail----->" + product);
		if (null != product) {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(true, "Product Details Updated Successfully", product), HttpStatus.OK);
		} else {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(false, "Unable To Update Product Details", product),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete-productdetails")
	public ResponseEntity<FlipcartProductResponse> deleteProductdetailsByProductcode(
			@RequestParam("productCode") String productCode) {
		logger.info("<---ProductCode --->" + productCode);
		boolean status = productService.deleteFlipcartProductByProductcode(productCode);
		if (status) {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(true, "Product Details Deleted Successfully", null), HttpStatus.OK);
		} else {
			return new ResponseEntity<FlipcartProductResponse>(
					new FlipcartProductResponse(false, "Unable To Delete Product Details", null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
