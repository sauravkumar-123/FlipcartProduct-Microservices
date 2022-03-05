package com.flipcart.ServiceImpl;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.flipcart.Dao.FlipcartProductRepository;
import com.flipcart.ExceptionHandle.FlipcartProductTypeException;
import com.flipcart.Model.FlipcartProduct;
import com.flipcart.Request.ProductDetailsRequest;
import com.flipcart.Service.FlipcartProductService;
import com.flipcart.Util.Utility;


@Service
public class FlipcartProductServiceImpl implements FlipcartProductService {

	private static final Logger logger=LoggerFactory.getLogger(FlipcartProductServiceImpl.class);
	
	@Autowired
	private FlipcartProductRepository flipcartProductRepository;
	
	@Override
	public String addProductFromManufacturer(List<Map<String, Object>> list) {
	String msg="";	
    try {	
	     list.parallelStream().forEach(i->{
		 FlipcartProduct flipcatproduct=new FlipcartProduct();
		 flipcatproduct.setProductbrand(Optional.of(i.get("ProductBrand").toString()).orElseThrow(NullPointerException::new));
		 flipcatproduct.setProductmfgdate(Optional.of(i.get("ProductMfgDate").toString()).orElseThrow(NullPointerException::new));
		 flipcatproduct.setProductname(Optional.of(i.get("ProductName").toString()).orElseThrow(NullPointerException::new));
		 flipcatproduct.setProductcode(Optional.of(i.get("ProductCode").toString()).orElseThrow(NullPointerException::new));
		 flipcatproduct.setProductid(Optional.of(i.get("ProductId").toString()).orElseThrow(NullPointerException::new));
		 flipcatproduct.setProductprice(Optional.of(Double.parseDouble(i.get("ProductPrice").toString())).orElseThrow(NullPointerException::new));
		 Double manufacturerPrice=Optional.of(Double.parseDouble(i.get("ProductPrice").toString())).orElseThrow(NullPointerException::new);
		 Double flipcartprice=Utility.flipcartSellingPrice(manufacturerPrice);
		 flipcatproduct.setFlipcartproductprice(Utility.roundAvoid(flipcartprice, 2));
		 flipcartProductRepository.save(flipcatproduct);
	   });
	   msg="success";  
	}catch(NullPointerException e) {
		e.printStackTrace();
		throw new FlipcartProductTypeException("Invalid data get from product manufacturer",HttpStatus.EXPECTATION_FAILED);
	}catch(Exception e){
		e.printStackTrace();
		throw new FlipcartProductTypeException("Unable To Save Data From Product Manufacturer");
	}
    return msg;
  }
	
	@Override
	public String addProductDetails(ProductDetailsRequest productDetailsRequest) throws ParseException {
		String msg="";
		FlipcartProduct productmfg=flipcartProductRepository.findByProductidOrProductcode(productDetailsRequest.getProductid(), productDetailsRequest.getProductcode());
        if(null!=productmfg) {
        	throw new FlipcartProductTypeException("Product Details Already Present!!!!- Try To Save Another Product", HttpStatus.ALREADY_REPORTED);
        }else {
        	FlipcartProduct productmnuf=new FlipcartProduct();
        	productmnuf.setProductid(productDetailsRequest.getProductid());
        	productmnuf.setProductname(productDetailsRequest.getProductname());
        	productmnuf.setProductcode(productDetailsRequest.getProductcode());
        	productmnuf.setProductbrand(productDetailsRequest.getProductbrand());
        	productmnuf.setProductmfgdate(Utility.StringToDateConvert(productDetailsRequest.getProductmfgdate(), "Unable To Save ManufacturerDate").toString());
        	productmnuf.setFlipcartproductprice(productDetailsRequest.getFlipcartproductprice());
        	logger.info("<------Product Details------>"+productmnuf);
        	flipcartProductRepository.save(productmnuf);
        	msg="success";
		}
       return msg;  
	}

	@Override
	public List<Map<String, Object>> getAllProduct() {
		List<Map<String,Object>> listData=new LinkedList<Map<String,Object>>();
		List<FlipcartProduct> productlist=flipcartProductRepository.findAll();
		logger.info("<------Product Details List------>"+productlist);
		if (null!=productlist && !productlist.isEmpty()) {
			Collections.sort(productlist, (o1,o2)-> o1.getProductcode().compareTo(o2.getProductcode()));
			productlist.parallelStream().forEach(l->{
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("CreatedDate", l.getCreatedDate());
				map.put("ProductId", l.getProductid());
				map.put("ProductName", l.getProductname());
				map.put("ProductCode", l.getProductcode());
				map.put("ProductBrand", l.getProductbrand());
				map.put("ProductMfgDate", l.getProductmfgdate());
				map.put("ProductPrice", l.getFlipcartproductprice());
				listData.add(map);
			});
		}else {
			throw new NullPointerException("Product Details Not Found");
		}
	  return listData;	
	}

	@Override
	public  List<Map<String, Object>> getProductByProductcode(String productcode) {
		List<Map<String,Object>> listData=new LinkedList<Map<String,Object>>();
		FlipcartProduct product=flipcartProductRepository.findByProductcode(productcode);
		logger.info("<------Product Details------>"+product);
		if(null!=product) {
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("CreatedDate", product.getCreatedDate());
			map.put("ProductId",   product.getProductid());
			map.put("ProductName", product.getProductname());
			map.put("ProductCode", product.getProductcode());
			map.put("ProductBrand",product.getProductbrand());
			map.put("ProductMfgDate",product.getProductmfgdate());
			map.put("ProductPrice", product.getFlipcartproductprice());
			listData.add(map);
		}else {
			throw new NullPointerException("Product Details Not Found");
		}
		return listData;		
	}

	@Override
	public String updateProductDetailsByProductcode(String productcode,ProductDetailsRequest productDetailsRequest) {
		String msg="";
		FlipcartProduct product=flipcartProductRepository.findByProductcode(productcode);
		if(null!=product) {
			product.setProductname(productDetailsRequest.getProductname());
			product.setProductbrand(productDetailsRequest.getProductbrand());
			product.setProductcode(productDetailsRequest.getProductcode());
			product.setFlipcartproductprice(productDetailsRequest.getFlipcartproductprice());
			logger.info("<------Product Details------>"+product);
			flipcartProductRepository.save(product);
			msg="success";
		}else {
			throw new NullPointerException("Product Not Found To Update Data");
		}
	 return msg;	
	}

	@Override
	public String deleteProductByProductcode(String productcode) {
		String msg="";
		FlipcartProduct product=flipcartProductRepository.findByProductcode(productcode);	
		if(null!=product) {
			logger.info("<------Product Details------>"+product);
			flipcartProductRepository.delete(product);
			msg="success";
		}else {
			throw new NullPointerException("Product Not Found To Delete Data!!!");
		}
	  return msg;	
	}

}
