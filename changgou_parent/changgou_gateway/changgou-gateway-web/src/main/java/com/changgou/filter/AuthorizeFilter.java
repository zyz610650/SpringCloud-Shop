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

    private static final String USER_LOGIN_URL="http://localhost:9001/oauth/login";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        ServerHttpRequest request=exchange.getRequest();
        ServerHttpResponse response=exchange.getResponse();

        String path=request.getURI().getPath();

        if (URLFilter.hasAuthorize(path))
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
            return needAuthorization(USER_LOGIN_URL,exchange);
        }

        try {
//            Claims claims= JwtUtil.parseJWT(tokent);
//            request.mutate().header(AUTHORIZE_TOKEN,claims.toString());
            if (!tokent.startsWith("bearer ")&&!tokent.startsWith("Bearer "))
                tokent="bearer "+tokent;
            request.mutate().header(AUTHORIZE_TOKEN,tokent);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }
    public Mono<Void> needAuthorization(String url,ServerWebExchange exchange)
    {
        ServerHttpResponse response=exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location",url);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
