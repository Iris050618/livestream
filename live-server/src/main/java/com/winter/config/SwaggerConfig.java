package com.winter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket commonDocket() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("公共模块")
                .apiInfo(new ApiInfoBuilder()
                        .title("公共模块")
                        .version("1.0").build())
                .select()
                .apis(
                        RequestHandlerSelectors
                                .basePackage("com.winner.common.controller")
                )
                .paths(PathSelectors.any()).build().globalRequestParameters(this.params());
    }

    @Bean
    public Docket meetingDocket() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("会议模块")
                .apiInfo(new ApiInfoBuilder()
                        .title("会议模块")
                        .version("1.0").build())
                .select()
                .apis(
                        RequestHandlerSelectors
                                .basePackage("com.winner.meeting.controller")
                )
                .paths(PathSelectors.any()).build().globalRequestParameters(this.params());
    }

    private List<RequestParameter> params() {
        RequestParameterBuilder builder = new RequestParameterBuilder();
        List<RequestParameter> pars = new ArrayList<>();
        builder.name("Authorization").description("user token")
                .in(ParameterType.HEADER)
                .required(false).build();
        pars.add(builder.build());
        return pars;
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("api文档")
                .description("document")
                .version("1.0")
                .build();
    }
}
