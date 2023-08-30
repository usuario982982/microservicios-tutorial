package com.usuario.service.configuracion;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/* Con esta anotación esta clase va a registrar beans en el contenedor de beans de Spring. */ 

@Configuration
public class RestTemplateConfig {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
