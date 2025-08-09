package com.jajaja.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "toss")
public class TossPaymentsConfig {
	private String approveURL;
	private String refundURL;
	private String clientApiKey;
	private String secretApiKey;
	private String secureKey;
}