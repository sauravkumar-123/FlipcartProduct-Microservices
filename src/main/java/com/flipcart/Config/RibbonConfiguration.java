package com.flipcart.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;

public class RibbonConfiguration {

	@Autowired
	IClientConfig iClientConfig;
	
//	@Bean
//	public IPing ping(IClientConfig ribbonclient) {
//		return new PingUrl();
//	}
	
	@Bean
	public IRule rule(IClientConfig ribbonclient) {
		return new AvailabilityFilteringRule();
	}

}
