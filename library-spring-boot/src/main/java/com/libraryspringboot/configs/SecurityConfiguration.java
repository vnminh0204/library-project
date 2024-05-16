package com.libraryspringboot.configs;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // Disable Cross Site Request Forgery (CSRF)
    http.csrf(AbstractHttpConfigurer::disable);

    // Protect endpoints at /api/<type>/secure
    http.authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/books/secure/**",
                "/api/reviews/secure/**",
                "/api/messages/secure/**",
                "/api/admin/secure/**")
            .authenticated()
        );

    http.oauth2ResourceServer(oauth2 -> oauth2
        .jwt(Customizer.withDefaults())
    );

    // Add CORS filters
    http.cors(cors -> { /* your CORS configuration here */ });

    // Add content negotiation strategy
    http.setSharedObject(ContentNegotiationStrategy.class,
        new HeaderContentNegotiationStrategy());

    // Force a non-empty response body for 401's to make the response friendly
    Okta.configureResourceServer401ResponseBody(http);

    return http.build();
  }
}
