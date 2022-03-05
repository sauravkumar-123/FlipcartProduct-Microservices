package com.flipcart.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.flipcart.ExceptionHandle.FlipcartProductTypeException;

public class Utility {

	public static Date StringToDateConvert(String date,String msg) {
		Date converteddate=null;
		try {
             converteddate= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date);
        }catch(Exception e){
        	e.printStackTrace();
        	throw new FlipcartProductTypeException(msg);
        }
	  return converteddate;
	}	
	
	public static double roundAvoid(double value, int places) {
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}
	
	public static double flipcartSellingPrice(double price) {
		if(price>=0 && price<=500) {
			return price;
		}else if (price>=501 && price<=5000) {
			price=price+((price*10)/100);
			return price;
		}else if (price>=5001 && price<=20000) {
			price=price+((price*20)/100);
			return price;
		}else if (price>=20001 && price<=80000) {
			price=price+((price*30)/100);
			return price;
	}else {
		price=price+((price*30)/100);
		return price;
	}
  }		
}
