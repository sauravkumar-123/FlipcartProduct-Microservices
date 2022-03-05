package com.flipcart;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.flipcart.Config.RibbonConfiguration;
import com.flipcart.ProxyServerFign.ProductManufacturerProxy;
import com.flipcart.ProxyServerFign.ProductManufacturerProxyImpl; 

@EnableFeignClients("com.flipcart")
@EnableDiscoveryClient
@SpringBootApplication
@RibbonClient(name="ProductManufacturer",configuration = RibbonConfiguration.class)
public class FlipcartProductApplication {

	@Value("${server.port}")
	private String portno;
	
	private static final Logger logger=LoggerFactory.getLogger(FlipcartProductApplication.class);
	
	public static void main(String[] args) {
		logger.info("<---------FlipcartProduct Project Start------------>");
		SpringApplication.run(FlipcartProductApplication.class, args);
		logger.info("<---------FlipcartProduct Project End------------>");
	}
	
	@PostConstruct
	public void init() {
		logger.info("<---------FlipcartProduct Server Run On PortNo:------------>"+portno);
	}
	
	  @Bean
	  @LoadBalanced
	  public RestTemplate restTemplate() {
	    return new RestTemplate();
	  }
	
	/*@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}*/
	  
	@Bean  
	public ProductManufacturerProxy productManufacturerProxy() {
		return new ProductManufacturerProxyImpl();
	  }
}
