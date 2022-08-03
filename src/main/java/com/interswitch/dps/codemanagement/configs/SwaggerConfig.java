package com.interswitch.dps.codemanagement.configs;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;
import java.util.Arrays;


@Configuration
@ComponentScan("com.interswitch.dps.codemanagement.configs")
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

	/*
	 * @Bean public Docket apiDocket() { final String swaggerToken = getToken();
	 * return new Docket(DocumentationType.SWAGGER_2).select()
	 * .apis(RequestHandlerSelectors.basePackage(
	 * "com.rensource.merchant.app.controllers"))
	 * .paths(regex("/api.*")).build().apiInfo(metaData())
	 * .globalOperationParameters(Arrays.asList(new
	 * ParameterBuilder().name("Authorization") .modelRef(new
	 * ModelRef("string")).parameterType("header").required(true).hidden(true)
	 * .defaultValue("Bearer " + swaggerToken).build()));
	 * 
	 * }
	 */

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.interswitch.dps.codemanagement.apis"))
				.paths(regex("/api.*")).build().apiInfo(metaData())
				.securitySchemes(Arrays.asList(apiKey()));
	}


	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}


	private ApiInfo metaData() {
		return new ApiInfoBuilder()
				.title("DPS 2.0 Code Management API Documentation.")
				.description("This API provides endpoints for DPS 2.0 code management platform.")
				.version("1.0.0")
				.license("Interswitch Systegra License Version 1.0")
				.contact(new Contact("Earnest Erihbra Suru", "https://www.linkedin.com/in/suru-earnest", "earnest.suru@interswitchgroup.com"))
				.build();
	}

	private ApiKey apiKey() {
		return new ApiKey("apiKey", "Authorization", "header");
	}
}
