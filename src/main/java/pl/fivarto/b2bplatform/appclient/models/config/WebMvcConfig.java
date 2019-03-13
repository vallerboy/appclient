package pl.fivarto.b2bplatform.appclient.models.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

   final HttpRequestInterceptor httpRequestValidator;

    @Autowired
    public WebMvcConfig(HttpRequestInterceptor sessionRequestValidator) {
        this.httpRequestValidator = sessionRequestValidator;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photos/**").addResourceLocations("file:photos/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpRequestValidator);
    }
}
