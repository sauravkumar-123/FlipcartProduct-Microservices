package com.flipcart.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FlipcartProductResponse {

	private boolean status;
	private String message;
	private Object datasource;
}
