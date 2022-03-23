package com.flipcart.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flipcart.DAO.FlipcartProductRepository;
import com.flipcart.ExceptionHandle.GlobalException;
import com.flipcart.Model.FlipcartProduct;
import com.flipcart.Service.FlipcartProductService;
import com.flipcart.Util.Utility;

@Service
public class FlipcartProductServiceImpl implements FlipcartProductService {

	private static final Logger logger = LoggerFactory.getLogger(FlipcartProductServiceImpl.class);

	@Autowired
	private FlipcartProductRepository flipcartProductRepository;

	@Transactional(rollbackOn = Exception.class)
	@Override
	public List<FlipcartProduct> addProductFromProductmanufacturer(List<Map<String, Object>> list) {
		List<FlipcartProduct> resultList = new ArrayList<>();
		logger.info("<---Response from ProductManufacturer--->" + list);
		list.parallelStream().forEach(product -> {
			FlipcartProduct flipcatproduct = new FlipcartProduct();
			try {
				flipcatproduct
						.setProductCode(Optional.of(product.get("productCode").toString()).orElseThrow(Exception::new));
				flipcatproduct
						.setProductName(Optional.of(product.get("productName").toString()).orElseThrow(Exception::new));
				flipcatproduct.setProductbrandName(
						Optional.of(product.get("productbrandName").toString()).orElseThrow(Exception::new));
				flipcatproduct.setModelNo(Optional.of(product.get("modelNo").toString()).orElseThrow(Exception::new));
				flipcatproduct.setSerialNo(Optional.of(product.get("serialNo").toString()).orElseThrow(Exception::new));
				flipcatproduct.setTotalStock(Optional.of(Integer.parseInt(product.get("totalStock").toString()))
						.orElseThrow(Exception::new));
				flipcatproduct.setAvaliableStock(Optional.of(Integer.parseInt(product.get("avaliableStock").toString()))
						.orElseThrow(Exception::new));
				flipcatproduct.setIsActive('Y');
				flipcatproduct.setExpDate(Optional.of(Utility.StringToDateConvert(product.get("expDate").toString()))
						.orElseThrow(Exception::new));
				flipcatproduct.setMfgDate(Optional.of(Utility.StringToDateConvert(product.get("mfgDate").toString()))
						.orElseThrow(Exception::new));

				Double manufacturerPrice = Optional.of(Double.parseDouble(product.get("productprice").toString()))
						.orElseThrow(Exception::new);
				flipcatproduct.setProductprice(Utility.roundAvoid(manufacturerPrice, 2));

				Double flipcartPrice = Utility.flipcartSellingPrice(manufacturerPrice);
				flipcatproduct.setFlipcartProductprice(Utility.roundAvoid(flipcartPrice, 2));

				flipcatproduct
						.setDescription(Optional.of(product.get("description").toString()).orElseThrow(Exception::new));
				FlipcartProduct savedProduct = flipcartProductRepository.save(flipcatproduct);
				resultList.add(savedProduct);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GlobalException("Server Error!! " + e.getMessage());
			}
		});
		logger.info("<---Flipcart Saved Product's--->" + resultList);
		return resultList;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public FlipcartProduct addFlipcartProductDetails(FlipcartProduct productDetailsRequest) {
		boolean isAvaliable = flipcartProductRepository.existsByProductCodeOrSerialNoAndIsActive(
				productDetailsRequest.getProductCode(), productDetailsRequest.getSerialNo(), 'Y');
		if (isAvaliable) {
			throw new GlobalException("Product Details Are Already Avaliable");
		} else {
			FlipcartProduct product = new FlipcartProduct();
			product.setProductCode(productDetailsRequest.getProductCode());
			product.setProductName(productDetailsRequest.getProductName());
			product.setProductbrandName(productDetailsRequest.getProductbrandName());
			product.setModelNo(productDetailsRequest.getModelNo());
			product.setSerialNo(productDetailsRequest.getSerialNo());
			product.setTotalStock(productDetailsRequest.getTotalStock());
			product.setAvaliableStock(productDetailsRequest.getAvaliableStock());
			product.setIsActive('Y');
			product.setMfgDate(productDetailsRequest.getMfgDate());
			product.setExpDate(productDetailsRequest.getExpDate());
			product.setProductprice(0.00);
			product.setFlipcartProductprice(productDetailsRequest.getFlipcartProductprice());
			product.setDescription(productDetailsRequest.getDescription());
			logger.info("flipcart Product Detail:{}" + product);
			return flipcartProductRepository.save(product);
		}
	}

	@Override
	public List<FlipcartProduct> getAllFlipcartProduct() {
		List<FlipcartProduct> productlist = flipcartProductRepository.findAll('Y');
		logger.info("<------flipcart Product Details List------>" + productlist);
		if (null != productlist && !productlist.isEmpty()) {
			Collections.sort(productlist, (p1, p2) -> p1.getProductprice().compareTo(p2.getProductprice()));
			return productlist;
		} else {
			throw new NullPointerException("Product Details Not Found");
		}
	}

	@Override
	public List<FlipcartProduct> getFlipcartProductBySerachKey(String searchKey) {
		List<FlipcartProduct> productList = flipcartProductRepository.findBySearchkey(searchKey, 'Y');
		if (null != productList && !productList.isEmpty()) {
			Collections.sort(productList, (p1, p2) -> p1.getProductprice().compareTo(p2.getProductprice()));
			return productList;
		} else {
			throw new NullPointerException("Products Details Not Found With Key: " + searchKey);
		}
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public FlipcartProduct updateFlipcartProductDetailsByProductcode(String productCode,
			FlipcartProduct productDetailsRequest) {
		Optional<FlipcartProduct> chkPoint = flipcartProductRepository.findByProductCodeAndIsActive(productCode, 'Y');
		if (chkPoint.isPresent()) {
			FlipcartProduct product = chkPoint.get();
			logger.info("Fetch Product Detail:{}" + product);
			product.setProductName(productDetailsRequest.getProductName());
			product.setProductbrandName(productDetailsRequest.getProductbrandName());
			product.setModelNo(productDetailsRequest.getModelNo());
			product.setTotalStock(productDetailsRequest.getTotalStock());
			product.setAvaliableStock(productDetailsRequest.getAvaliableStock());
			product.setMfgDate(productDetailsRequest.getMfgDate());
			product.setExpDate(productDetailsRequest.getExpDate());
			product.setFlipcartProductprice(productDetailsRequest.getFlipcartProductprice());
			product.setDescription(productDetailsRequest.getDescription());
			logger.info("Updated Product Detail:{}" + product);
			return flipcartProductRepository.save(product);
		} else {
			throw new NullPointerException("Product Details Not Avaliable For productCode: " + productCode);
		}
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public boolean deleteFlipcartProductByProductcode(String productcode) {
		boolean status = false;
		Optional<FlipcartProduct> chkPoint = flipcartProductRepository.findByProductCodeAndIsActive(productcode, 'Y');
		if (chkPoint.isPresent()) {
			FlipcartProduct product = chkPoint.get();
			product.setIsActive('N');
			status = true;
		} else {
			throw new NullPointerException("Product Details Not Avaliable For productCode: " + productcode);
		}
		return status;
	}

}
