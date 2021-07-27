package com.changgou.entity;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Configuration
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        try {
            ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes==null) return;
            HttpServletRequest request=attributes.getRequest();
            Enumeration<String> headerNames=request.getHeaderNames();
            if (headerNames==null) return;
            while(headerNames.hasMoreElements())
            {
                String name=headerNames.nextElement();
                String values=request.getHeader(name);
                template.header(name,values);
                List<String> list=new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
