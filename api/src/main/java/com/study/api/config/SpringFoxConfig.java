package com.study.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author chenglutao
 */
@EnableSwagger2
@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket docket(Environment environment){

        //设置要显示swagger环境
        Profiles profiles = Profiles.of("test");
        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //如果为false则在浏览器中无法访问
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.study"))
                //paths:过滤什么路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Swagger API Document")
                .description("API Document")
                .termsOfServiceUrl("https://blog.csdn.net/qq_36805343/article/details/103599407")
                .version("1.0")
                .build();
    }
}
