package com.changgou.filter;

import com.changgou.util.JwtUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN="Authorization";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("11");
        ServerHttpRequest request=exchange.getRequest();
        ServerHttpResponse response=exchange.getResponse();

        String path=request.getURI().getPath();

        if (path.startsWith("/api/user/login")||path.startsWith("/api/brand/search/"))
        {
            Mono<Void> filter=chain.filter(exchange);
            return filter;
        }

        String tokent=request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        if (StringUtils.isEmpty(tokent)) tokent=request.getQueryParams().getFirst(AUTHORIZE_TOKEN);

        HttpCookie cookie=request.getCookies().getFirst(AUTHORIZE_TOKEN);
        if (cookie!=null)
        {
            tokent=cookie.getValue();
        }

        if (StringUtils.isEmpty(tokent))
        {
            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            return response.setComplete();
        }

        try {
            Claims claims= JwtUtil.parseJWT(tokent);
            request.mutate().header(AUTHORIZE_TOKEN,claims.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
