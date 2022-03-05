package com.flipcart.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.flipcart.Model.FlipcartProduct;

@Repository
public interface FlipcartProductRepository extends JpaRepository<FlipcartProduct, Integer>{

	@Query("SELECT p FROM FlipcartProduct p WHERE p.productid =:productid OR p.productcode =:productcode")
	public FlipcartProduct findByProductidOrProductcode(@Param(value = "productid") String productid,@Param(value = "productcode") String productcode);
	@Query("SELECT p FROM FlipcartProduct p WHERE p.productcode =:productcode")
	public FlipcartProduct findByProductcode(@Param(value = "productcode") String productcode);
}
