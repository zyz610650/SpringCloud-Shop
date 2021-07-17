package com.changgou.item.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Configuration
public class EnableMvcConfig implements WebMvcConfigurer {

    @Override
    public void  addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/items/**").addResourceLocations("classpath:/templates/items/");
    }
}
