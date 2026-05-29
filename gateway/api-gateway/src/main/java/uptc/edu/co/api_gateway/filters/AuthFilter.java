package uptc.edu.co.api_gateway.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private final SecretKey key;
    private final String gatewaySecret;
    private final ObjectMapper objectMapper;

    public AuthFilter(
            @Value("${jwt.secret}") String secretBase64,
            @Value("${gateway.secret}") String gatewaySecret,
            ObjectMapper objectMapper) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
        this.gatewaySecret = gatewaySecret;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Token missing or invalid format");
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String subject = claims.getSubject();
            Object scopesObj = claims.get("scopes");

            String scopes = (scopesObj instanceof Collection<?>)
                    ? String.join(",",
                            ((Collection<?>) scopesObj)
                                    .stream()
                                    .map(Object::toString)
                                    .toList())
                    : "";

            // Mutate the request adding headers
            exchange = exchange.mutate()
                    .request(r -> r.headers(headers -> {
                        headers.add("X-User", subject);
                        headers.add("X-Scopes", scopes);
                        headers.add("X-Gateway-Secret", this.gatewaySecret);
                    }))
                    .build();

        } catch (JwtException ex) {
            return unauthorized(exchange, "Invalid or expired token");
        }

        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(
            ServerWebExchange exchange,
            String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        try {
            String body = objectMapper.writeValueAsString(
                    Map.of(
                            "status", 401,
                            "error", "Unauthorized",
                            "message", message));

            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(body.getBytes(StandardCharsets.UTF_8));

            return exchange.getResponse().writeWith(Mono.just(buffer));

        } catch (JsonProcessingException e) {
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublicPath(String path) {
        return path.equals("/ms-auth/auth/login")
                || path.equals("/ms-auth/auth/register")
                || path.startsWith("/ms-auth/auth/oauth2")
                || path.startsWith("/ms-auth/actuator");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}