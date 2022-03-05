package com.flipcart.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Flipcart_Product")
public class FlipcartProduct extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Product_Index")
	private int index_id;
	
	@Column(name = "Flipcart_ProductId", length = 20)
	private String productid;
	
	@Column(name = "Flipcart_ProductName", length = 50)
	private String productname;
	
	@Column(name = "Flipcart_ProductCode", length = 20)
	private String productcode;
	
	@Column(name = "Flipcat_ProductBrand", length = 30)
	private String productbrand;
	
	@Column(name = "Flipcart_ProductMfgDate")
	private String productmfgdate;
	
	@Column(name = "Warehouse_ProductPrice")
	private double productprice;
	
	@Column(name = "Flipcart_ProductPrice")
	private double flipcartproductprice;
	
}
