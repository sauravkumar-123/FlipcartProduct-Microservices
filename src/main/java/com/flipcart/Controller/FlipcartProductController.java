package com.flipcart.Controller;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipcart.ExceptionHandle.FlipcartProductTypeException;
import com.flipcart.ProxyServerFign.ProductManufacturerProxy;
import com.flipcart.Request.ProductDetailsRequest;
import com.flipcart.Response.FlipcartProductResponse;
import com.flipcart.Response.ProductManufacturerResponse;
import com.flipcart.Service.FlipcartProductService;

import io.swagger.annotations.Api;

@Api(value = "FlipcartProductController" ,description = "This is FlipcartProduct Controller for communicate with ProductManufacturer API")
@RestController
@RequestMapping(value = "/v1/flipcartproductapi")
public class FlipcartProductController {

	private static final Logger logger=LoggerFactory.getLogger(FlipcartProductController.class);
	
	@Autowired
	private FlipcartProductService productService;
	
	@Autowired
	private ProductManufacturerProxy proxyserverdata;
	
	@GetMapping("/get-product-from-manufacturer")
	public ResponseEntity<FlipcartProductResponse> getProductDetailsFromManufacturer(){
	
		ProductManufacturerResponse manufacturerResponse=proxyserverdata.getProductDetailsFromManufacturer();
		if(null!=manufacturerResponse && manufacturerResponse.isStatus()) {
			try {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list= (List<Map<String, Object>>) manufacturerResponse.getData();
				logger.info("<---Response from ProductManufacturer--->"+list);
				String result=productService.addProductFromManufacturer(list);
				if (result.equalsIgnoreCase("success")) {
				    return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), true, "Product Details Saved", result),HttpStatus.CREATED);	
			     }
               /*******************************************************************
               JSONArray json_arr=new JSONArray();
               JSONObject json_obj=new JSONObject();
			    for (Map<String, Object> map : list) {
			        for (Map.Entry<String, Object> entry : map.entrySet()) {
			            String key = entry.getKey();
			            Object value = entry.getValue();
			            try {
			                json_obj.put(key,value);
			            } catch (JSONException e) {
			                // TODO Auto-generated catch block
			                e.printStackTrace();
			            }                           
			        }
			        json_arr.put(json_obj);
			    }*********************************************************************/		
			} catch (Exception e) {
				e.printStackTrace();
				throw new FlipcartProductTypeException("Error!!! while fetch data from productManufacturer server");
			}
		}else {
			return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "Data Not Found From ProductManufacturer!!Please Check", null),HttpStatus.NOT_FOUND);
		}
	return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "Unable To Save Product Details", null),HttpStatus.EXPECTATION_FAILED);
	}
	
	@PostMapping("/add-productdetails")
	public ResponseEntity<FlipcartProductResponse> addProductDetails(@RequestBody ProductDetailsRequest productDetailsRequest) throws ParseException{
		logger.info("<---Request Payload--->"+productDetailsRequest);
		if(null!=productDetailsRequest.getProductid() && !productDetailsRequest.getProductid().isEmpty() &&
		   null!=productDetailsRequest.getProductname() && !productDetailsRequest.getProductname().isEmpty()&&
		   null!=productDetailsRequest.getProductcode() && !productDetailsRequest.getProductcode().isEmpty() &&
		   null!=productDetailsRequest.getProductbrand() && !productDetailsRequest.getProductbrand().isEmpty() &&
		   null!=productDetailsRequest.getProductmfgdate() && !productDetailsRequest.getProductmfgdate().isEmpty()) {
		 String result=productService.addProductDetails(productDetailsRequest);
		     if (result.equalsIgnoreCase("success")) {
			    return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), true, "Product Details Saved", result),HttpStatus.CREATED);	
		     }
		}else {
			return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "Input Field Missing!!Please Check", null),HttpStatus.BAD_REQUEST);
		}
	return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "Unable To Save Product Details", null),HttpStatus.EXPECTATION_FAILED);
	}
	
	@GetMapping("/get-all-productdetails")
	public ResponseEntity<FlipcartProductResponse> getProductDetails(){
		List<Map<String, Object>> prodList=productService.getAllProduct();
		logger.info("<-----Product List----->"+prodList);
		if(null!=prodList && !prodList.isEmpty()) {
			 return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), true, "Product Details List Fetched", prodList),HttpStatus.OK);
		}else {
			return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "Data Not Found", prodList),HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/get-productdetail-by-productcode/{productcode}")
	public ResponseEntity<FlipcartProductResponse> getProductDetailByProductcode(@PathVariable(value = "productcode") String productcode){
	 if (null!=productcode) {
		List<Map<String, Object>> prodList=productService.getProductByProductcode(productcode);
		logger.info("<-----Product Detail----->"+prodList);
		if(null!=prodList && !prodList.isEmpty()) {
			 return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), true, "Product Detail Fetched", prodList),HttpStatus.OK);
		}else {
			return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "Data Not Found", prodList),HttpStatus.NOT_FOUND);
		}
	 }else {
		 return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "ProductCode Missing!!Please Check", null),HttpStatus.BAD_REQUEST);	 
	 }
	}
	
	@PutMapping("/update-productdetail-by-productcode/{productcode}")
	public ResponseEntity<FlipcartProductResponse> updateProductDetail(@PathVariable(value = "productcode") String productcode,@RequestBody ProductDetailsRequest productDetailsRequest){
	 if (null!=productcode) {	
		logger.info("<---Request Payload--->"+productDetailsRequest);
		if(null!=productDetailsRequest.getProductname() && !productDetailsRequest.getProductname().isEmpty()&&
		   null!=productDetailsRequest.getProductcode() && !productDetailsRequest.getProductcode().isEmpty() &&
		   null!=productDetailsRequest.getProductbrand() && !productDetailsRequest.getProductbrand().isEmpty()) {
		 String result=productService.updateProductDetailsByProductcode(productcode, productDetailsRequest);
		     if (result.equalsIgnoreCase("success")) {
			    return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), true, "Product Details Updated", result),HttpStatus.OK);	
		     }
		}else {
			return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "Input Parameter Missing!!Please Check", null),HttpStatus.BAD_REQUEST);
		}
	 }else {
		 return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "ProductCode Missing!!Please Check", null),HttpStatus.BAD_REQUEST);
	 }
  return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "Error While Update Data", null),HttpStatus.INTERNAL_SERVER_ERROR);	 
 }
	
	
	@DeleteMapping("/delete-productdetail-by-productcode/{productcode}")
	public ResponseEntity<FlipcartProductResponse> deleteProductDetail(@PathVariable(value = "productcode") String productcode){
	 if (null!=productcode) {	
		 String result=productService.deleteProductByProductcode(productcode);
		     if (result.equalsIgnoreCase("success")) {
			    return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), true, "Product Details deleted Sucessful", result),HttpStatus.OK);	
		     }
	}else {
		return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "ProductCode Missing!!Please Check", null),HttpStatus.BAD_REQUEST);	
	}
 return new ResponseEntity<FlipcartProductResponse>(new FlipcartProductResponse(ZonedDateTime.now(), false, "Error While Delete Data", null),HttpStatus.INTERNAL_SERVER_ERROR);	 
 }	 
}
