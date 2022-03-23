package com.flipcart.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.flipcart.ExceptionHandle.GlobalException;

public class Utility {

	public static Date StringToDateConvert(String inputDate) {
		Date converteddate = null;
		try {
			converteddate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(inputDate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Date Conversion Error!!");
		}
		return converteddate;
	}

	public static double roundAvoid(double value, int places) {
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}

	public static double flipcartSellingPrice(double price) {
		if (price >= 0 && price <= 500) {
			return price;
		} else if (price >= 501 && price <= 5000) {
			price = price + ((price * 5) / 100);
			return price;
		} else if (price >= 5001 && price <= 10000) {
			price = price + ((price * 10) / 100);
			return price;
		} else if (price >= 10001 && price <= 50000) {
			price = price + ((price * 20) / 100);
			return price;
		} else if (price >= 50001 && price <= 100000) {
			price = price + ((price * 30) / 100);
			return price;
		} else {
			price = price + ((price * 7) / 100);
			return price;
		}
	}
}
