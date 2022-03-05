package com.flipcart.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsRequest {
	
	private String productid;
	private String productname;
	private String productcode;
	private String productbrand;
	private String productmfgdate;
	private double productprice;
	private double flipcartproductprice;
}
