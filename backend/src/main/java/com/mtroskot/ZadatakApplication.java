package com.mtroskot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class ZadatakApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ZadatakApplication.class, args);
	}

	@Bean
	public MessageSource messageResource() {
		ResourceBundleMessageSource messageBundleResrc=new ResourceBundleMessageSource();
		messageBundleResrc.setBasename("classpath:messages.message");
		return messageBundleResrc;
	}
}
